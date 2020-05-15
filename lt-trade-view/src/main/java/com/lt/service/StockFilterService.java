package com.lt.service;

import com.alibaba.fastjson.JSON;
import com.lt.entity.FundReal;
import com.lt.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import similar.DataNode;
import similar.PearsonCorrelationScore;
import similar.Similarity;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description 股票筛选
 * @date 2020/5/14
 */
@Slf4j
@Service
public class StockFilterService {

    @Autowired
    FundService fundService;
    List<String> times =  Arrays.asList("09:30","09:31","09:32","09:33","09:34","09:35","09:36","09:37","09:38","09:39","09:40","09:41","09:42","09:43","09:44","09:45","09:46","09:47","09:48","09:49","09:50","09:51","09:52","09:53","09:54","09:55","09:56","09:57","09:58","09:59","10:00","10:01","10:02","10:03","10:04","10:05","10:06","10:07","10:08","10:09","10:10","10:11","10:12","10:13","10:14","10:15","10:16","10:17","10:18","10:19","10:20","10:21","10:22","10:23","10:24","10:25","10:26","10:27","10:28","10:29","10:30","10:31","10:32","10:33","10:34","10:35","10:36","10:37","10:38","10:39","10:40","10:41","10:42","10:43","10:44","10:45","10:46","10:47","10:48","10:49","10:50","10:51","10:52","10:53","10:54","10:55","10:56","10:57","10:58","10:59","11:00","11:01","11:02","11:03","11:04","11:05","11:06","11:07","11:08","11:09","11:10","11:11","11:12","11:13","11:14","11:15","11:16","11:17","11:18","11:19","11:20","11:21","11:22","11:23","11:24","11:25","11:26","11:27","11:28","11:29","11:30");
    List<String> chgs = Arrays.asList("-0.0013","-0.0025","-0.0025","-0.0051","-0.0051","-0.0051","-0.0025","-0.0025","-0.0013","-0.0013","-0.0013","-0.0013","-0.0025","-0.0013","-0.0013","-0.0025","-0.0025","-0.0025","-0.0025","-0.0025","-0.0013","-0.0025","-0.0013","-0.0025","-0.0013","-0.0025","-0.0013","-0.0025","-0.0025","-0.0025","-0.0013","-0.0025","-0.0013","-0.0013","-0.0013","-0.0013","-0.0013","-0.0025","-0.0025","-0.0025","-0.0025","-0.0025","-0.0038","-0.0038","-0.0025","-0.0025","-0.0025","-0.0025","-0.0038","-0.0038","-0.0038","-0.0038","-0.0038","-0.0051","-0.0051","-0.0038","-0.0038","-0.0038","-0.0038","-0.0038","-0.0051","-0.0051","-0.0051","-0.0051","-0.0064","-0.0051","-0.0038","-0.0051","-0.0038","-0.0025","-0.0025","-0.0025","-0.0025","-0.0025","-0.0038","-0.0038","-0.0038","-0.0038","-0.0038","-0.0051","-0.0051","-0.0025","-0.0025","-0.0051","-0.0064","-0.0064","-0.0051","-0.0064","-0.0051","-0.0051","-0.0051","-0.0051","-0.0064","-0.0025","-0.0025","0.0038","0.0102","0.0089","0.0076","0.0076","0.0089","0.0076","0.0102","0.0102","0.0115","0.014","0.0102","0.0076","0.0076","0.0076","0.0051","0.0051","0.0051","0.0076","0.0076","0.0064","0.0051","0.0038","0.0051","0.0051","0.0051");

    /**
     * 股票过滤
     * @return
     */
    public List<String> filterStockCodes(){
        Date afterDate = TimeUtil.StringToDate("09:29","HH:mm");
        String createTime = LocalDate.now().toString();
        String lastDate = "2020-04-03";
        List<FundReal> fundReals = fundService.selectByTarget(createTime,lastDate);
        List<Similarity> resultAll = new ArrayList<>();
        for(FundReal real : fundReals){
            String clinch_change_minute = real.getClinchChangeMinute();
            Map<String,Double> realityMap = (Map<String, Double>) JSON.parse(clinch_change_minute);

            List<Double> chgList = replenishTime(realityMap);

            double similarityRido = calculateSimilarity(chgList);
            Similarity similarity = Similarity.builder()
                    .code(real.getStockCode())
                    .similarityRido(similarityRido)
                    .similaritySize(realityMap.size())
                    .build();
            resultAll.add(similarity);
        }

        resultAll = resultAll.stream().sorted(Comparator.comparing(Similarity::getSimilarityRido).reversed()).collect(Collectors.toList());
        for(int i =0;i < resultAll.size();i++){
            System.out.println(JSON.toJSONString(resultAll.get(i))+"======"+i);
        }
        System.out.println(resultAll.size()+"================");
        return null;
    }

    /**
     * 补充一分钟内没有成交的价格变动数据
     * @param realityMap
     * @return
     */
    private List<Double> replenishTime(Map<String,Double> realityMap){
        List<Double> chgList = new ArrayList<>();
        Map<String,Double> resultMap = new HashMap();
        for(int i = 0;i < times.size();i++){
            String time = times.get(i);
            Double chg = realityMap.get(time);
            if(null == chg){
                String beforTime = TimeUtil.getaLimitTime(time,-1);//获取上一分钟时间
                resultMap.put(time,resultMap.get(beforTime));
                chgList.add(resultMap.get(beforTime));
                continue;
            }
            resultMap.put(time,chg);
            chgList.add(chg);
        }
        return chgList;
    }

    /**
     * 计算数据相似度
     * @return
     */
    private double calculateSimilarity(List<Double> chgList){
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
}
