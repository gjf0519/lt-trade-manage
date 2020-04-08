package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.utils.BigDecimalUtil;
import com.lt.utils.RestTemplateUtil;
import com.lt.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description
 * @date 2020/4/7
 */
@Slf4j
public class FundDetailThread implements Runnable {
    private int sn;
    private String key;
    private List<String> codes;
    private CountDownLatch latch;
    private RedisTemplate<String, String> redisTemplate;

    public FundDetailThread(String key,
                            int sn,
                            List<String> codes,
                            CountDownLatch latch,
                            RedisTemplate<String, String> redisTemplate) {
        this.key = key;
        this.sn = sn;
        this.codes = codes;
        this.latch = latch;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run() {
        List<FundReal> fundReals = RealFundDetailAm.getFundReals();
        if(null == fundReals){
            log.info("实时资金流向数据未处理完成");
            latch.countDown();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<ClinchDetail> list = null;
        for (String code : codes) {
            FundReal fundReal = null;
            for(FundReal item : fundReals){
                if (item.getStockCode().equals(code)){
                    fundReal = item;
                    break;
                }
            }
            list = new ArrayList<>();
            for (int i = 0; ; i++) {
                String result = RestTemplateUtil.get("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c=" + code + "&p=" + i + "", null);
                if (StringUtils.isBlank(result)) {
                    break;
                }
                String[] details = result.split("=");
                String fundsStr = details[1];
                fundsStr = fundsStr.substring(3, fundsStr.length() - 2);
                String[] fundsArray = fundsStr.split("\\|");
                ClinchDetail detail = null;
                for (String str : fundsArray) {
                    detail = new ClinchDetail();
                    String[] fund = str.split("/");
                    detail.setClinchTime(TimeUtil.StringToDate(fund[1], formatter));
                    detail.setClinchPrice(Double.valueOf(fund[2]));
                    detail.setClinchChange(Double.valueOf(fund[3]));
                    detail.setClinchQuantity(Integer.valueOf(fund[4]));
                    detail.setClinchSum(Double.valueOf(fund[5]));
                    switch (fund[6]) {
                        case "S":
                            detail.setClinchNature(0);
                            break;
                        case "B":
                            detail.setClinchNature(1);
                            break;
                        default:
                            detail.setClinchNature(2);
                    }
                    list.add(detail);
                }
            }
            if (list.isEmpty()) {
                continue;
            }
            FundEntity fundEntity = null;
            switch (sn){
                case 0:{
                    //上午指标
                    fundEntity = transformAmCapital(list, code,fundReal);
                    break;
                }case 1:{
                    //当天指标
                    fundEntity = transformAllCapital(list, code);
                    break;
                }
            }
            if(null == fundEntity){
                log.info("交易明细信息未收集成功:{}",code);
                return;
            }
            redisTemplate.opsForList().rightPushAll(key, JSON.toJSONString(fundEntity));
        }
        latch.countDown();
    }

    /**
     * 到中午的交易明细
     * @param list
     * @param code
     * @return
     */
    private FundEntity transformAmCapital(List<ClinchDetail> list, String code,FundReal fundReal){
        //按每分钟对数据进行分组
        Map<Date,List<ClinchDetail>> timeGroup = list.stream()
                .sorted(Comparator.comparing(ClinchDetail::getClinchTime))
                .collect(Collectors.groupingBy(ClinchDetail::getClinchTime));
        //去重分钟交易手数
        Set<Integer> handMinute = null;
        //上午重复数据占比
        Set<Integer> handAm = new HashSet<>();
        int handAllAm = 0;
        double openPrice = fundReal.getOpenPrice();
        Map<String,Double> priceMinute = new HashMap<>();
        for(Map.Entry<Date,List<ClinchDetail>> entry : timeGroup.entrySet()){
            handMinute = new HashSet<>();
            List<ClinchDetail> vars = entry.getValue();
            for(ClinchDetail detail : vars){
                handAllAm++;
                handMinute.add(detail.getClinchQuantity());
                //计算每分钟价格变动
                handAm.add(detail.getClinchQuantity());
            }
            if(code.equals("sh600758")){
                log.info("时间:{};价格:{}",TimeUtil.dateFormat(entry.getKey(),"HH:mm"),vars.get(0).getClinchPrice());
            }
            //每分钟涨跌幅
            double chgPrice = BigDecimalUtil.sub(BigDecimalUtil.div(vars.get(0).getClinchPrice(),openPrice,4),1,4);
            priceMinute.put(TimeUtil.dateFormat(entry.getKey(),"HH:mm"),chgPrice);
        }
        //计算上午重复手数占比
        double repetitionAm = BigDecimalUtil.div(handAm.size(),handAllAm,2);
        repetitionAm = 1 - repetitionAm;

        return FundEntity.builder()
                .stockCode(code)
                .createTime(LocalDate.now().toString())
                .clinchChangeMinute(JSON.toJSONString(priceMinute))
                .repetitionAmPct(repetitionAm)
                .dealNum(list.size())
                .build();
    }

    private FundEntity transformAllCapital(List<ClinchDetail> list, String code){
        if (null == list || list.isEmpty()){
            log.info("没有交易明细记录:{}",code);
            return null;
        }
        //去重当天交易手数
        Set<Integer> handAll = new HashSet<>();
        for(ClinchDetail item : list){
            handAll.add(item.getClinchQuantity());
        }
        //计算当天重复手数占比
        double repetitionAll = BigDecimalUtil.div(handAll.size(),list.size(),2);
        repetitionAll = 1 - repetitionAll;

        return FundEntity.builder()
                .stockCode(code)
                .createTime(LocalDate.now().toString())
                .redoAllPct(repetitionAll)
                .build();
    }
}
