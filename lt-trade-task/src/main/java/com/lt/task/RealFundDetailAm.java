package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.FundEntity;
import com.lt.utils.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description 实时交易明细数据获取
 * @date 2020/4/1
 */
@Slf4j
public class RealFundDetailAm {

    @Scheduled(cron = "0 40 11 * * ? ")// 0/1 * * * * *
    public void execute() {
        List<String> codes = Constants.STOCK_CODE;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<ClinchDetail> list = null;
        for(String code : codes){
            list = new ArrayList<>();
            for(int i = 0;;i++){
                String result = RestTemplateUtil.get("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c="+code+"&p="+i+"",null);
                if(StringUtils.isBlank(result)){
                    break;
                }
                String [] details = result.split("=");
                String fundsStr = details[1];
                fundsStr = fundsStr.substring(3,fundsStr.length()-2);
                String [] fundsArray = fundsStr.split("\\|");
                ClinchDetail detail = null;
                for(String str : fundsArray){
                    detail = new ClinchDetail();
                    String [] fund = str.split("/");
                    detail.setClinchTime(TimeUtil.StringToDate(fund[1],formatter));
                    detail.setClinchPrice(Double.valueOf(fund[2]));
                    detail.setClinchChange(Double.valueOf(fund[3]));
                    detail.setClinchQuantity(Integer.valueOf(fund[4]));
                    detail.setClinchSum(Double.valueOf(fund[5]));
//                    detail.setClinchNature(fund[6]);
                    list.add(detail);
                }
            }
            if(list.isEmpty()){
                continue;
            }
            FundEntity fundEntity = transformAmCapital(list,code);
            FileWriteUtil.writeTXT(FileWriteUtil.getTextPath(),"lt_fund_detail_am",JSON.toJSONString(fundEntity));
        }
        log.info("=============================上午资金指标收集完成========================");
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
        List<RealMinute> minutes = new ArrayList<>();
        RealMinute realMinute = null;
        for(Map.Entry<Date,List<ClinchDetail>> entry : timeGroup.entrySet()){
            realMinute = new RealMinute();
            handMinute = new HashSet<>();
            List<ClinchDetail> vars = entry.getValue();
            double clinchChangeMinute = 0;
            for(ClinchDetail detail : vars){
                handAllAm++;
                handMinute.add(detail.getClinchQuantity());
                clinchChangeMinute = BigDecimalUtil.add(clinchChangeMinute,detail.getClinchChange(),2);
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

    @Data
    @Builder
    @NoArgsConstructor
    static class RealMinute {
        private String reaTime;
        private double clinchChangeMinute;
    }
}
