package com.lt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lt.entity.FundReal;
import com.lt.service.FundService;
import com.lt.utils.BigDecimalUtil;
import com.lt.utils.TimeUtil;
import com.lt.vo.DailyBasicVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description
 * @date 2020/4/2
 */
@SpringBootTest
public class FundTest {

    @Autowired
    FundService fundService;

    List<String> times =  Arrays.asList("09:30","09:31","09:32","09:33","09:34","09:35","09:36","09:37","09:38","09:39","09:40","09:41","09:42","09:43","09:44","09:45","09:46","09:47","09:48","09:49","09:50","09:51","09:52","09:53","09:54","09:55","09:56","09:57","09:58","09:59","10:00","10:01","10:02","10:03","10:04","10:05","10:06","10:07","10:08","10:09","10:10","10:11","10:12","10:13","10:14","10:15","10:16","10:17","10:18","10:19","10:20","10:21","10:22","10:23","10:24","10:25","10:26","10:27","10:28","10:29","10:30","10:31","10:32","10:33","10:34","10:35","10:36","10:37","10:38","10:39","10:40","10:41","10:42","10:43","10:44","10:45","10:46","10:47","10:48","10:49","10:50","10:51","10:52","10:53","10:54","10:55","10:56","10:57","10:58","10:59","11:00","11:01","11:02","11:03","11:04","11:05","11:06","11:07","11:08","11:09","11:10","11:11","11:12","11:13","11:14","11:15","11:16","11:17","11:18","11:19","11:20","11:21","11:22","11:23","11:24","11:25","11:26","11:27","11:28","11:29","11:30");
    List<String> chgs = Arrays.asList("-0.0013","-0.0025","-0.0025","-0.0051","-0.0051","-0.0051","-0.0025","-0.0025","-0.0013","-0.0013","-0.0013","-0.0013","-0.0025","-0.0013","-0.0013","-0.0025","-0.0025","-0.0025","-0.0025","-0.0025","-0.0013","-0.0025","-0.0013","-0.0025","-0.0013","-0.0025","-0.0013","-0.0025","-0.0025","-0.0025","-0.0013","-0.0025","-0.0013","-0.0013","-0.0013","-0.0013","-0.0013","-0.0025","-0.0025","-0.0025","-0.0025","-0.0025","-0.0038","-0.0038","-0.0025","-0.0025","-0.0025","-0.0025","-0.0038","-0.0038","-0.0038","-0.0038","-0.0038","-0.0051","-0.0051","-0.0038","-0.0038","-0.0038","-0.0038","-0.0038","-0.0051","-0.0051","-0.0051","-0.0051","-0.0064","-0.0051","-0.0038","-0.0051","-0.0038","-0.0025","-0.0025","-0.0025","-0.0025","-0.0025","-0.0038","-0.0038","-0.0038","-0.0038","-0.0038","-0.0051","-0.0051","-0.0025","-0.0025","-0.0051","-0.0064","-0.0064","-0.0051","-0.0064","-0.0051","-0.0051","-0.0051","-0.0051","-0.0064","-0.0025","-0.0025","0.0038","0.0102","0.0089","0.0076","0.0076","0.0089","0.0076","0.0102","0.0102","0.0115","0.014","0.0102","0.0076","0.0076","0.0076","0.0051","0.0051","0.0051","0.0076","0.0076","0.0064","0.0051","0.0038","0.0051","0.0051","0.0051");
    /**
     * 计算五日涨幅
     */
    @Test
    void calculate() {
        Date afterDate = TimeUtil.StringToDate("09:29","HH:mm");
        Date date = TimeUtil.StringToDate("11:30","HH:mm");
//        String createTime = LocalDate.now().toString();
        String createTime = "2020-04-03";
        String lastDate = "2020-04-02";
        List<FundReal> fundReals = fundService.selectByTarget(createTime,lastDate);
        List<Similarity> resultAll = new ArrayList<>();
        for(FundReal real : fundReals){
//            if(!real.getStockCode().equals("sh603555")){
//                continue;
//            }
            String clinch_change_minute = real.getClinchChangeMinute();
            List<RealMinute> list = JSONArray.parseArray(clinch_change_minute,RealMinute.class);
            list = list.stream().sorted(Comparator.comparing(RealMinute::getReaTime)).collect(Collectors.toList());
            double price = real.getOpenPrice();
            Map<String,RealMinute> map = new HashMap();
            for(RealMinute item : list){
                if(TimeUtil.StringToDate(item.getReaTime(),"HH:mm").before(date)
                    && TimeUtil.StringToDate(item.getReaTime(),"HH:mm").after(afterDate)){
                    price = BigDecimalUtil.add(price,item.getClinchChangeMinute(),4);
                    double chg = BigDecimalUtil.sub(BigDecimalUtil.div(price,real.getOpenPrice(),4),1,4);
                    if(chg > 0.02){
                        map = null;
                        break;
                    }
                    item.setPriceChg(chg);
                    map.put(item.getReaTime(),item);
                }
            }

            if(map == null){
                continue;
            }

            List<RealMinute> resultList = new ArrayList<>();
            Map<String,RealMinute> resultMap = new HashMap();
            for(int i = 0;i < times.size();i++){
                String time = times.get(i);
                RealMinute item = map.get(time);
                if(null == item){
                    String beforTime = getBeforTime(time);
                    item = new RealMinute();
                    item.setPriceChg(resultMap.get(beforTime).getPriceChg());
                    item.setReaTime(time);
                    resultMap.put(time,item);
                    resultList.add(item);
                    continue;
                }
                resultMap.put(time,item);
                resultList.add(item);
            }

            List<String> chgListStrs = new ArrayList<>();
            List<Double> chgList = new ArrayList<>();
            for (int i = 0;i < resultList.size();i++){
                chgList.add(resultList.get(i).getPriceChg());
                chgListStrs.add(String.valueOf(resultList.get(i).getPriceChg()));
            }
//            System.out.println(JSON.toJSONString(chgListStrs));

            double similarityRido = pearson(chgList);
            Similarity similarity = new Similarity();
            similarity.setCode(real.getStockCode());
            similarity.setSimilarityRido(similarityRido);
            similarity.setSimilaritySize(list.size());
            resultAll.add(similarity);
        }

        List<Similarity> resultAll1 = resultAll.stream().sorted(Comparator.comparing(Similarity::getSimilarityRido).reversed()).collect(Collectors.toList());
        for(int i =0;i < resultAll1.size();i++){
            System.out.println(JSON.toJSONString(resultAll1.get(i))+"======"+i);
        }
        System.out.println(resultAll1.size()+"================");
    }

    double pearson(List<Double> chgList){
        double [] var1 = new double[chgs.size()];
        for(int i = 0;i < chgs.size();i++){
            var1[i] = Double.valueOf(chgs.get(i));
        }
        DataNode x = new DataNode(var1);
        double [] var2 = new double[chgList.size()];
        for(int i = 0;i < chgList.size();i++){
            var2[i] = chgList.get(i);
        }
        DataNode y = new DataNode(var2);
        PearsonCorrelationScore score = new PearsonCorrelationScore(x, y);
        return score.getPearsonCorrelationScore();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Similarity {
        private String code;
        private double similarityRido;
        private int similaritySize;
    }

    String getBeforTime(String time){
        Date date=TimeUtil.StringToDate(time,"HH:mm");
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -1);//获取上一分钟
        Date beforeD = cal.getTime();
        String nextTime = new SimpleDateFormat("HH:mm").format(beforeD);
        return nextTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class RealMinute {
        private String reaTime;
        private double clinchChangeMinute;
        private double priceChg;
    }

    @Test
    void calculateFivePctChg() {
//        String createTime = LocalDate.now().toString();
        String createTime = "2020-04-03";
        List<String> codes = fundService.queryDailyBasic(createTime);
        for(String code : codes){
//            if(!code.equals("sz000014")){
//                continue;
//            }
            List<DailyBasicVo> vars = fundService.queryByStockCode(code,createTime);
            if(null == vars || vars.isEmpty()){
                System.out.println("=========================="+code);
                continue;
            }
            //计算5日差价
            double five_div = BigDecimalUtil.sub(vars.get(0).getClose(),vars.get(vars.size()-1).getClose(),2);
            boolean isFu = false;
            if(five_div < 0){
                five_div = five_div*-1;
                isFu = true;
            }
            double five_pct_chg = BigDecimalUtil.div(five_div,vars.get(vars.size()-1).getClose(),2);
            if (isFu){
                five_pct_chg = five_pct_chg*-1;
            }
            fundService.updateFivePctChg(code,five_pct_chg,createTime);
        }
    }

    /**
     * 计算五日平均成交笔数与当天成交笔数 频率
     */
//    @Test
//    void calculateFiveVolumeRatio() {
////        String createTime = LocalDate.now().toString();
//        String createTime = "2020-04-03";
//        List<String> codes = fundService.queryFundDetail(createTime);
//        for(String code : codes){
//            List<FundDetail> vars = fundService.queryDealNum(code,createTime);
//            int dealNum = 0;
//            for(int i = 1;i < vars.size();i++){
//                FundDetail detail = vars.get(i);
//                dealNum = dealNum + detail.getDealNum();
//            }
//            int size = vars.size()-1;
//            if(size == 0){
//                System.out.println(code+"========================");
//                continue;
//            }
//            double dealNumAvg = BigDecimalUtil.div(dealNum,size,2);
//            double five_volume_ratio = BigDecimalUtil.div(vars.get(0).getDealNum(),dealNumAvg,2);
//            fundService.updateFiveVolumeRatio(code,five_volume_ratio,createTime);//lt_fund_detail 表
//        }
//    }

    public static void main(String[] args) {
        List<String> times = new ArrayList<>();
        String time = "9:30";
        for(int i = 0;i <= 120;i++){
            Date date=TimeUtil.StringToDate(time,"HH:mm");
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, 1);// 5分钟之前的时间
            Date beforeD = cal.getTime();
            time = new SimpleDateFormat("HH:mm").format(beforeD);
            times.add("0.0001");
        }
        System.out.println(JSON.toJSONString(times));
    }
}
