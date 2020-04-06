package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.lt.entity.ClinchDetail;
import com.lt.entity.DailyBasic;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.service.FundService;
import com.lt.utils.BigDecimalUtil;
import com.lt.utils.Constants;
import com.lt.utils.RestTemplateUtil;
import com.lt.utils.TimeUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@SpringBootTest
class RealFundExtractTest {

    @Autowired
    RealFundExtract realFundExtract;
    @Autowired
    FundService fundService;
    @Autowired
    DownLoadGif downLoadGif;

    @Test
    void execute() throws ParseException {
        realFundExtract.execute();
    }

    /**
     * 中午过滤股票
     */
    @Test
    void execute1() {
        //根据涨跌幅过滤
        List<FundReal> list = fundService.selectByTarget("2020-03-30");
        //根据成交量过滤
        //'sh600086','sh600311','sh600766','sh603222','sz002731'
        List<DailyBasic> vars = fundService.queryByStockCode("600086");
        //计算5日差价
        double five_pct_chg = Double.valueOf(vars.get(0).getClose()) - Double.valueOf(vars.get(vars.size()-1).getClose());
        for(DailyBasic item : vars){
            item.getClose();
        }
    }

    @Test
    void downLoadGif() {
        downLoadGif.execute();
    }

    @Test
    void detailAm() throws ParseException {
        List<String> codes = Constants.STOCK_CODE;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<ClinchDetail> list = null;
        for(String code : codes){
            list = new ArrayList<>();
            for(int i = 0;;i++){
                System.out.println(code+"=========="+i);
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
                    detail.setClinchTime(formatter.parse(fund[1]));
                    detail.setClinchPrice(Double.valueOf(fund[2]));
                    detail.setClinchChange(Double.valueOf(fund[3]));
                    detail.setClinchQuantity(Integer.valueOf(fund[4]));
                    detail.setClinchSum(Double.valueOf(fund[5]));
//            detail.setClinchNature(fund[6]);
                    list.add(detail);
                }
            }
            if(list.isEmpty()){
                continue;
            }
            FundEntity fundEntity = transformCapital(list,code);
            fundService.saveRepetitive(fundEntity);
        }
    }

    @Test
    void detailAmOne() throws ParseException {
        List<ClinchDetail> list = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        for(int i = 0;;i++){
            String result = RestTemplateUtil.get("http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c=sz002731&p="+i+"",null);
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
                detail.setClinchTime(formatter.parse(fund[1]));
                detail.setClinchPrice(Double.valueOf(fund[2]));
                detail.setClinchChange(Double.valueOf(fund[3]));
                detail.setClinchQuantity(Integer.valueOf(fund[4]));
                detail.setClinchSum(Double.valueOf(fund[5]));
                detail.setClinchNature(fund[6]);
                list.add(detail);
            }
        }
        if(list.isEmpty()){
            return;
        }

//        Map<String,IntSummaryStatistics> map = list.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchNature,Collectors.summarizingInt(ClinchDetail::getClinchQuantity)));
//        for(Map.Entry<String,IntSummaryStatistics> item : map.entrySet()){
//            System.out.println(item.getKey()+"=========="+item.getValue().getSum());
//        }
//        Map<String,DoubleSummaryStatistics> map = list.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchNature,Collectors.summarizingDouble(ClinchDetail::getClinchSum)));
//        for(Map.Entry<String,DoubleSummaryStatistics> item : map.entrySet()){
//            BigDecimal d1 = new BigDecimal(Double.toString(item.getValue().getSum()));
//            BigDecimal d2 = new BigDecimal(Integer.toString(1));
//            System.out.println(item.getKey()+"=========="+d1.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString());
//        }

        Map<String,List<ClinchDetail>> map = list.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchNature));
        List<Map<String,String>> dadan = new ArrayList<>();
        for(Map.Entry<String,List<ClinchDetail>> item : map.entrySet()){
            Map<String,String> m = new HashMap<>();
            //大于10万
            int var1 = 0;
            //大于50万
            int var2 = 0;
            //大于100万
            int var3 = 0;
            for(ClinchDetail detail : item.getValue()){
                if(detail.getClinchSum() > 200000){
                    var1++;
                };
                if(detail.getClinchSum() > 500000){
                    var2++;
                };
                if(detail.getClinchSum() > 1000000){
                    System.out.println("===============");
                    var3++;
                };
            }
            double dvar1 = BigDecimalUtil.div(var1,item.getValue().size(),4);
            double dvar2 = BigDecimalUtil.div(var2,item.getValue().size(),4);
            double dvar3 = BigDecimalUtil.div(var3,item.getValue().size(),4);
            m.put(item.getKey(),dvar1+","+dvar2+","+dvar3);
            dadan.add(m);
        }

        System.out.println(JSON.toJSONString(dadan));

//        FundEntity fundEntity = transformCapital(list,"sz002731");
//        fundEntity.setLargeOrder(JSON.toJSONString(dadan));
//        fundService.saveRepetitive(fundEntity);
    }

    private static FundEntity transformCapital(List<ClinchDetail> list, String code){
        //按每分钟对数据进行分组
        Map<Date,List<ClinchDetail>> timeGroup = list.stream().collect(Collectors.groupingBy(ClinchDetail::getClinchTime));
        Map<String,Double> redoMinuteMap = new HashMap<>();
        //去重分钟交易手数
        Set<Integer> handMinute = null;
        //上午重复数据占比
        Set<Integer> handAm = new HashSet<>();
        int handAllAm = 0;
        //每分钟重复数据占比
        for(Map.Entry<Date,List<ClinchDetail>> entry : timeGroup.entrySet()){
            handMinute = new HashSet<>();
            List<ClinchDetail> vars = entry.getValue();
            for(ClinchDetail detail : vars){
                handAllAm++;
                handAm.add(detail.getClinchQuantity());
                handMinute.add(detail.getClinchQuantity());
            }
            //计算每分钟重复手数占比
            double repetition = BigDecimalUtil.div(handMinute.size(),vars.size(),2);
            repetition = 1 - repetition;
            redoMinuteMap.put(TimeUtil.dateFormat(entry.getKey(),"HH:mm"),repetition);
        }
        //计算上午重复手数占比
        double repetitionAm = BigDecimalUtil.div(handAm.size(),handAllAm,2);
        repetitionAm = 1 - repetitionAm;

        return FundEntity.builder()
                .stockCode(code)
                .createTime(LocalDate.now().toString())
                .redoMinutePct(JSON.toJSONString(redoMinuteMap))
                .repetitionAmPct(repetitionAm)
                .dealNum(list.size())
                .build();
    }

//    http://stock.gtimg.cn/data/index.php?appn=detail&action=data&c=sh601857&p=7

}