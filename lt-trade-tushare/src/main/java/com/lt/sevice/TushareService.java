package com.lt.sevice;

import com.alibaba.fastjson.JSON;
import com.lt.entity.TushareResult;
import com.lt.mapper.TushareMapper;
import com.lt.utils.RestTemplateUtil;
import com.lt.utils.TimeUtil;
import com.lt.utils.TushareUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@Service
public class TushareService {

    @Autowired
    TushareMapper tushareMapper;

    /**
     *股票基本信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void stockBasic(){
        //股票基本信息
        Map<String,Object> params = new HashMap<>();
        params.put("is_hs", "N");
        params.put("exchange", "SSE");
        params.put("list_status", "L");
        params.put("api_name", "stock_basic");
        params.put("token", TushareUtil.TUSHARE_TOKEN);
        params.put("fields", "ts_code,symbol,name,area,industry,market,list_status,is_hs");
        String res = RestTemplateUtil.post(TushareUtil.URL,JSON.toJSONString(params),null);
        TushareResult tushareResult = JSON.parseObject(res, TushareResult.class);
        List<Map<String,Object>> results = TushareUtil.resultBuild(tushareResult);
        tushareMapper.saveStockBasic(results);
    }

    /**
     *每日指标
     */
    public void transactionDetail(){
        //交易明细
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> item = new HashMap<>();
//        String tradeDate = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
        String tradeDate = "20200331";
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
        List<Map<String,Object>> dailys =transactionDaily();
//        String date = LocalDate.now().toString();
        String date = "2020-03-31";
        for(int i = 0;i < results.size();i++){
            Map<String,Object> map = results.get(i);
            map.put("trade_date",date);
            Object obj = map.get("ts_code");
            for(int y = 0;y < dailys.size();y++){
                Map<String,Object> ite = dailys.get(y);
                if (obj.equals(ite.get("ts_code"))){
                    map.put("amount",ite.get("amount"));
                    map.put("pct_chg",ite.get("pct_chg"));
                }
            }
        }
        tushareMapper.saveDailyBasic(results);
    }

    /**
     *每日指标
     */
    public List<Map<String,Object>> transactionDaily(){
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> item = new HashMap<>();
//        String tradeDate = TimeUtil.dateFormat(new Date(),"yyyyMMdd");
        String tradeDate = "20200331";
        item.put("trade_date", tradeDate);
        params.put("params", item);
        params.put("api_name", "daily");
        params.put("token", TushareUtil.TUSHARE_TOKEN);
        params.put("fields", "ts_code,pct_chg,amount");
        String res = RestTemplateUtil.post(TushareUtil.URL,JSON.toJSONString(params),null);
        TushareResult tushareResult = JSON.parseObject(res, TushareResult.class);
        List<Map<String,Object>> results = TushareUtil.resultBuild(tushareResult);
        return results;
    }

    @Transactional
    public void updateClose(String code,String tradeDate,String close){
        tushareMapper.updateClose(code,tradeDate,close);
    };
}
