package com.lt.service;

import com.alibaba.fastjson.JSONArray;
import com.lt.entity.DealRecord;
import com.lt.entity.GatherRecord;
import com.lt.utils.BigDecimalUtil;
import com.lt.utils.Constants;
import com.lt.vo.DailyBasicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author gaijf
 * @description 抽取统计数据
 * @date 2020/5/14
 */
@Slf4j
@Service
public class StatService {
    @Autowired
    FundService fundService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 持久化成交明细数据
     */
    public void saveDealRecord(){
        List<String> codes = Constants.STOCK_CODE;
        for (String code : codes) {
            List<String> results = redisTemplate.opsForList().range("deal-record-"+code, 0, -1);
            for(String item : results){
                List<GatherRecord> records = JSONArray.parseArray(item,GatherRecord.class);
                if(records.isEmpty()){
                    continue;
                }
                fundService.saveGatherRecord(records);
            }
            int dealAllNum = TransformService.getDealAllNum(code);
            if(0 == dealAllNum){
                continue;
            }
            int dealAllSoleNum = TransformService.getDealAllSoleNum(code);
            DealRecord dealRecord = DealRecord.builder()
                    .stockCode(code)
                    .dealAllNum(dealAllNum)
                    .dealAllSoleNum(dealAllSoleNum)
                    .repetitionPct(BigDecimalUtil.div(dealAllSoleNum,dealAllNum,2))
                    .createTime(LocalDate.now().toString())
                    .build();
            fundService.saveStatDeal(dealRecord);
        }
    }

    /**
     * 计算过去五日涨幅
     */
    public void calculateFivePctChg() {
        String createTime = LocalDate.now().toString();
        List<String> codes = Constants.STOCK_CODE;
        for(String code : codes){
            List<DailyBasicVo> vars = fundService.queryByStockCode(code,createTime);
            if(null == vars || vars.isEmpty()){
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
     * 计算当天成交量与过去5日平均成交量占比
     */
    public void calculateFiveVolumeRatio() {
        String createTime = LocalDate.now().toString();
        List<String> codes = Constants.STOCK_CODE;
        for(String code : codes){
            int dealNum = fundService.queryDealNum(code,createTime);
            double dealNumAvg = BigDecimalUtil.div(dealNum,5,2);
            double five_volume_ratio = BigDecimalUtil.div(TransformService.getDealAllNum(code),dealNumAvg,2);
            fundService.updateFiveVolumeRatio(code,five_volume_ratio,createTime);//lt_fund_detail 表
        }
    }

}
