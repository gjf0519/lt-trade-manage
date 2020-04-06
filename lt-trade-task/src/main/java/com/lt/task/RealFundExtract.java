package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.FundReal;
import com.lt.utils.Constants;
import com.lt.utils.FileWriteUtil;
import com.lt.utils.RealCodeUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gaijf
 * @description 实时资金流向定时任务
 * @date 2020/2/24
 */
@Slf4j
public class RealFundExtract {
    private static final String url = "http://qt.gtimg.cn/q=";
    private static final int splitSize = 400;
    private static final String prefix = "ff_";
    private static final String startAm = "09:30:00";
    private static final String endAm = "11:30:00";
    private static final String startPm = "12:59:59";
    private static final String endPm = "24:00:00";
    private static final String timeFormat = "HH:mm:ss";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Scheduled(cron = "0 40 11 * * ? ")// 0/1 * * * * *
    public void execute() {
        List<String> codes = RealCodeUtil.getCodesStr(splitSize,Constants.STOCK_CODE,prefix);
        for (int i = 0; i < codes.size(); i++) {
            threadPoolExecutor.execute(new RealThread(codes.get(i)));
        }
        long count = threadPoolExecutor.getTaskCount()-threadPoolExecutor.getCompletedTaskCount();
        System.out.println("RealFundExtract:总数:"+threadPoolExecutor.getTaskCount()+"完成:"+
                threadPoolExecutor.getCompletedTaskCount()+"等待:"+count+"线程数量:"+threadPoolExecutor.getPoolSize());
    }

    private class RealThread implements Runnable {
        private String codes;
        public RealThread(String codes){
            this.codes = codes;
        }
        @Override
        public void run() {
            try {
                ResponseEntity<String> entity = restTemplate.getForEntity(url+codes,String.class);
                if (null == entity || entity.getBody() == null){
                    return;
                }
                codes = codes.replaceAll(prefix,"");
                ResponseEntity<String> change = restTemplate.getForEntity("http://qt.gtimg.cn/q="+codes,String.class);
                Map<String,RealMarket> pctChgs = resultSplit(change.getBody());

                StringTokenizer token = new StringTokenizer(entity.getBody(),";");
                while(token.hasMoreTokens()){
                    String result = token.nextToken();
                    if (StringUtils.isEmpty(result.trim()))
                        continue;
                    String[] values = result.split("~");
                    if(values.length < 9)
                        continue;
                    String code = values[0].split("=")[1];
                    code = code.substring(1,code.length());
                    String pctCode = code.substring(2,code.length());
                    RealMarket realMarket = pctChgs.get(pctCode);
                    double amount = Double.valueOf(values[9]);
                    FundReal fundEntity = FundReal.builder()
                            .stockCode(code)
                            .openPrice(realMarket.getOpenPrice())
                            .makersFundIn(Double.valueOf(values[1]))
                            .makersFundOut(Double.valueOf(values[2]))
                            .makersInFlow(Double.valueOf(values[3]))
                            .retailFuntIn(Double.valueOf(values[5]))
                            .retailFuntOut(Double.valueOf(values[6]))
                            .retailInFlow(Double.valueOf(values[7]))
                            .amounts(amount)
                            .pctChg(realMarket.getPctChg())
                            .exchange(realMarket.getExchange())
                            .createTime(LocalDate.now().toString())
                            .build();
                    FileWriteUtil.writeTXT(FileWriteUtil.getTextPath(),"lt_fund_real", JSON.toJSONString(fundEntity));
                    try{
                        redisTemplate.opsForList().rightPushAll("lt_fund_real",JSON.toJSONString(fundEntity));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
                log.info("实时资金数据获取异常",e);
            }
        }

        /**
         * 按照特定的规则拆分数据
         * @return
         */
        public Map<String,RealMarket> resultSplit(String results){
            Map<String,RealMarket> map = new HashMap<>();
            StringTokenizer token = new StringTokenizer(results,";");
            while(token.hasMoreTokens()){
                String result = token.nextToken();
                if (StringUtils.isEmpty(result.trim()))
                    continue;
                String[] values = result.split("~");
                if(values.length < 38)
                    continue;
                String chg = StringUtils.isEmpty(values[32]) ? "0":values[32];
                String exchange = StringUtils.isEmpty(values[38]) ? "0":values[38];
                String openPrice = StringUtils.isEmpty(values[5]) ? "0":values[5];
                RealMarket realMarket = RealMarket.builder()
                        .openPrice(Double.valueOf(openPrice))
                        .pctChg(Double.valueOf(chg))
                        .exchange(Double.valueOf(exchange))
                        .build();
                map.put(values[2],realMarket);
            }
            return map;
        }
    }

    @Data
    @Builder
    static class RealMarket {
        private double pctChg;
        private double exchange;
        private double openPrice;
    }
}