package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.utils.BigDecimalUtil;
import com.lt.utils.RestTemplateUtil;
import com.lt.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

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
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<ClinchDetail> list = null;
        for (String code : codes) {
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
                    fundEntity = transformAmCapital(list, code);
                    break;
                }case 1:{
                    //当天指标
                    fundEntity = transformAllCapital(list, code);
                    break;
                }
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
    private FundEntity transformAmCapital(List<ClinchDetail> list, String code){
        //按每分钟对数据进行分组
        Map<Date,List<ClinchDetail>> timeGroup = list.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchTime));
        //去重分钟交易手数
        Set<Integer> handMinute = null;
        //上午重复数据占比
        Set<Integer> handAm = new HashSet<>();
        int handAllAm = 0;
        List<FundDetailThread.RealMinute> minutes = new ArrayList<>();
        FundDetailThread.RealMinute realMinute = null;
        for(Map.Entry<Date,List<ClinchDetail>> entry : timeGroup.entrySet()){
            realMinute = new FundDetailThread.RealMinute();
            handMinute = new HashSet<>();
            List<ClinchDetail> vars = entry.getValue();
            double clinchChangeMinute = 0;
            for(ClinchDetail detail : vars){
                handAllAm++;
                handMinute.add(detail.getClinchQuantity());
                clinchChangeMinute = BigDecimalUtil.add(clinchChangeMinute,detail.getClinchChange(),2);
                handAm.add(detail.getClinchQuantity());
            }
            //计算价格变动
            realMinute.setClinchChangeMinute(clinchChangeMinute);
            realMinute.setReaTime(TimeUtil.dateFormat(entry.getKey(),"HH:mm"));
            minutes.add(realMinute);
        }
        //计算上午重复手数占比
        double repetitionAm = BigDecimalUtil.div(handAm.size(),handAllAm,2);
        repetitionAm = 1 - repetitionAm;

        return FundEntity.builder()
                .stockCode(code)
                .createTime(LocalDate.now().toString())
                .clinchChangeMinute(JSON.toJSONString(minutes))
                .repetitionAmPct(repetitionAm)
                .dealNum(list.size())
                .build();
    }

    class RealMinute {
        private String reaTime;
        private double clinchChangeMinute;

        public String getReaTime() {
            return reaTime;
        }

        public void setReaTime(String reaTime) {
            this.reaTime = reaTime;
        }

        public double getClinchChangeMinute() {
            return clinchChangeMinute;
        }

        public void setClinchChangeMinute(double clinchChangeMinute) {
            this.clinchChangeMinute = clinchChangeMinute;
        }
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
