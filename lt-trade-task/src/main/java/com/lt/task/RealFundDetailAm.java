package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.FundRecord;
import com.lt.entity.GatherFund;
import com.lt.entity.NeteaseFund;
import com.lt.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description 网易财经交易明细数据
 * @date 2020/4/14
 */
@Slf4j
public class RealFundDetailAm {
    private static final int splitSize = 400;
    private static final String startAm = "09:30:00";
    private static final String endAm = "11:30:00";
    private static final String timeFormat = "HH:mm:ss";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    private static final Map<String,Double> openPrices = new HashMap<>(3000);

    @Scheduled(cron = "0 */5 * * * ?")// 0/1 * * * * *
    public void execute() throws ParseException {
        String time = TimeUtil.dateFormat(new Date(),"HH:mm");
        if (TimeUtil.isEffectiveDate(startAm,endAm,timeFormat)){
            List<List<String>> codes = RealCodeUtil.getCodesList(splitSize,Constants.STOCK_CODE,null);
            for (int i = 0; i < codes.size(); i++) {
                threadPoolExecutor.execute(new RealFundDetailAm.RealThread(time,codes.get(i)));
            }
        }else {
            openPrices.clear();
            long count = threadPoolExecutor.getTaskCount()-threadPoolExecutor.getCompletedTaskCount();
            System.out.println("RealFundExtract:总数:"+threadPoolExecutor.getTaskCount()+"完成:"+
                    threadPoolExecutor.getCompletedTaskCount()+"等待:"+count+"线程数量:"+threadPoolExecutor.getPoolSize());
            while (count > 0){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = threadPoolExecutor.getTaskCount()-threadPoolExecutor.getCompletedTaskCount();
            }
            log.info("========================交易明细数据收集完成==========================");
        }
    }

    private class RealThread implements Runnable {
        private String time;
        private List<String> codes;
        public RealThread(String time,List<String> codes){
            this.time = time;
            this.codes = codes;
        }
        @Override
        public void run() {
            for(String code : codes){
                String subCode = code.substring(2,code.length());
                String fund = RestTemplateUtil.get("http://quotes.money.163.com/service/zhubi_ajax.html?symbol="+subCode+"&end="+time+":00", null);
                NeteaseFund neteaseFund = JSON.parseObject(fund,NeteaseFund.class);
                List<FundRecord> reals = neteaseFund.getZhubi_list();
                reals = reals.stream().sorted(Comparator.comparing(FundRecord::getDATE_STR)).collect(Collectors.toList());
                if(null == reals || reals.isEmpty()){
                    continue;
                }
                if(!openPrices.containsKey(code)){
                    openPrices.put(code,Double.valueOf(reals.get(0).getPRICE()));
                }
                List<String> itemPrice = new ArrayList<>();
                String realTime = reals.get(0).getDATE_STR().substring(0,reals.get(0).getDATE_STR().length()-3);
                double chgPrice = BigDecimalUtil.sub(BigDecimalUtil.div(Double.valueOf(reals.get(0).getPRICE()),openPrices.get(code),4),1,4);
                itemPrice.add(realTime+"="+reals.get(0).getPRICE());
                //过滤成交手数重复量
                Set<String> dealSoleNum = new HashSet<>();
                for (FundRecord record : reals){
                    dealSoleNum.add(record.getVOLUME_INC());
                    String dateStr = record.getDATE_STR();
                    dateStr = dateStr.substring(0,dateStr.length()-3);
                    if(realTime.equals(dateStr)){
                        continue;
                    }
                    realTime = dateStr;
                    chgPrice = BigDecimalUtil.sub(BigDecimalUtil.div(Double.valueOf(record.getPRICE()),openPrices.get(code),4),1,4);
                    itemPrice.add(realTime+"="+chgPrice);
                }
                GatherFund gatherFund = GatherFund.builder()
                        .pchChg(JSON.toJSONString(itemPrice))
                        .dealNum(reals.size())
                        .dealSoleNum(dealSoleNum.size())
                        .build();
                String key = "real-fund-163-"+code;
                redisTemplate.opsForList().rightPushAll(key, JSON.toJSONString(gatherFund));
                redisTemplate.expire(key, 20 * 60 * 60, TimeUnit.SECONDS);
            }
        }
    }
}