package com.lt;

import com.alibaba.fastjson.JSON;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.service.FundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description 解析生成的text文件
 * @date 2020/4/1
 */
@SpringBootTest
public class ParseZip {
    @Autowired
    FundService fundService;

    @Test
    void saveAmZip(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new Date());
        saveFundZip(dateString);
        saveFundDetailZip(dateString);
    }

    @Test
    void savePmZip(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new Date());
//        String dateString = "20200403";
        saveFundDetailAllZip(dateString);
        saveDailyZip(dateString);
    }

    /**
     * 中午资金情况
     * @param dateString
     */
    void saveFundZip(String dateString){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:\\zip\\"+dateString+"\\lt_fund_real.txt")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                FundReal fundReal =  JSON.parseObject(lineTxt, FundReal.class);
                fundService.saveFund(fundReal);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }

    /**
     * 交易明细
     * @param dateString
     */
    void saveFundDetailZip(String dateString){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:\\zip\\"+dateString+"\\lt_fund_detail_am.txt")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                FundEntity fundEntity =  JSON.parseObject(lineTxt,FundEntity.class);
                fundService.saveRepetitive(fundEntity);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }

    /**
     * 当天资金情况
     * @param dateString
     */
    void saveFundDetailAllZip(String dateString){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File("E:\\zip\\"+dateString+"\\lt_fund_detail_all.txt")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                FundEntity fundEntity =  JSON.parseObject(lineTxt,FundEntity.class);
                fundService.updateRepetitive(fundEntity);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }

    /**
     * 每日指标
     */
    void saveDailyZip(String dateString){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:\\zip\\"+dateString+"\\lt_daily_basic.txt")),
                    "UTF-8"));
            String lineTxt = null;
            List<Map<String,Object>> dailys = new ArrayList<>();
            while ((lineTxt = br.readLine()) != null) {
                Map map =  JSON.parseObject(lineTxt, Map.class);
                dailys.add(map);
            }
            fundService.saveDailyBasic(dailys);
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }
}
