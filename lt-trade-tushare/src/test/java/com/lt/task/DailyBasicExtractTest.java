package com.lt.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lt.entity.TushareResult;
import com.lt.mapper.TushareMapper;
import com.lt.sevice.TushareService;
import com.lt.utils.RestTemplateUtil;
import com.lt.utils.TimeUtil;
import com.lt.utils.TushareUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gaijf
 * @description
 * @date 2020/2/27
 */
@SpringBootTest
class DailyBasicExtractTest {

    @Autowired
    DailyBasicExtract dailyBasicExtract;
    @Autowired
    TushareMapper tushareMapper;
    @Autowired
    TushareService tushareService;

    @Test
    void execute() {
        dailyBasicExtract.execute();
//        transactionDetail();
    }

    public void transactionDetail(){
        //交易明细
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> item = new HashMap<>();
//        String tradeDate = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
        String tradeDate = "20200402";
        item.put("trade_date", tradeDate);
        params.put("params", item);
        params.put("api_name", "daily_basic");
        params.put("token", TushareUtil.TUSHARE_TOKEN);
        params.put("fields", "ts_code,close,trade_date,turnover_rate,turnover_rate_f,volume_ratio,ps,ps_ttm,total_share" +
                ",float_share,free_share,total_mv,circ_mv");
        String res = RestTemplateUtil.post(TushareUtil.URL,JSON.toJSONString(params),null);
        TushareResult tushareResult = JSON.parseObject(res, TushareResult.class);
        List<Map<String,Object>> results = TushareUtil.resultBuild(tushareResult);
        if(null == results || results.isEmpty()){
            System.out.println("交易指标没有更新的数据");
        }
        for (Map<String,Object> map : results){
            if(map.get("ts_code").toString().equals("sz000014")){
                System.out.println(JSON.toJSONString(map));
            }
//            tushareService.updateClose(map.get("ts_code").toString(),"2020-04-01",map.get("close").toString());
        }
    }


    @Test
    void saveZip(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:\\zip\\20200331\\lt_daily_basic.txt")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                Map map =  JSON.parseObject(lineTxt, Map.class);
                List<Map<String,Object>> dailys = new ArrayList<>();
                dailys.add(map);
                tushareMapper.saveDailyBasic(dailys);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }
}