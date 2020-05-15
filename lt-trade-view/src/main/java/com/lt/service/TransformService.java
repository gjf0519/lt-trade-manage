package com.lt.service;

import com.alibaba.fastjson.JSON;
import com.lt.entity.*;
import com.lt.utils.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaijf
 * @description 转换整理数据
 * @date 2020/4/7
 */
@Service
public class TransformService {

    @Autowired
    FundService fundService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //开盘价格
    private static final Map<String,Double> OPEN_PRICES = new HashMap<>(3000);
    //全部成交总数
    private static final Map<String,Integer> DEAL_ALL_NUM = new HashMap<>(3000);
    //去重成交总数
    private static final Map<String,Set<String>> DEAL_ALL_SOLE_NUM = new HashMap<>(3000);

    /**
     * 成交明细数据清洗转换
     */
    @Async
    public void transformDealRecord(RealDeal realDeal) {
        NeteaseFund neteaseFund = JSON.parseObject(realDeal.getRecordStrs(),NeteaseFund.class);
        List<FundRecord> records = neteaseFund.getZhubi_list();
        if(null == records || records.isEmpty()){
            return;
        }
        records = records.stream().sorted(Comparator.comparing(FundRecord::getDATE_STR)).collect(Collectors.toList());
        String code = realDeal.getCode();
        if(!OPEN_PRICES.containsKey(code)){
            OPEN_PRICES.put(code,Double.valueOf(records.get(0).getPRICE()));
            DEAL_ALL_NUM.put(code,records.size());
            DEAL_ALL_SOLE_NUM.put(code,new HashSet<>());
        }else {
            int num = DEAL_ALL_NUM.get(code)+records.size();
            DEAL_ALL_NUM.put(code,num);
        }
        //每分钟基本信息
        List<GatherRecord> gatherFunds = this.minuteDealInfo(code,records);
        //计算每分钟涨幅
        for(GatherRecord fund : gatherFunds){
            double chgPrice = BigDecimalUtil.sub(
                    BigDecimalUtil.div(Double.valueOf(fund.getPrice()), OPEN_PRICES.get(code), 4), 1, 4);
            fund.setPchChg(chgPrice);
        }
        redisTemplate.opsForList().rightPushAll("deal-record-"+code, JSON.toJSONString(gatherFunds));
    }

    /**
     * 每分钟基本信息
     */
    public List<GatherRecord> minuteDealInfo(String code, List<FundRecord> records){
        List<GatherRecord> gatherFunds = new ArrayList<>();
        String dateStr = records.get(0).getDATE_STR();
        String realTime = dateStr.substring(0, dateStr.length() - 3);
        //重复成交记录数量
        Set<String> dealSoleNum = new HashSet<>(records.size());
        //全部成交记录数量
        List<String> dealNum = new ArrayList<>(records.size());
        for(int i = 0;i < records.size();i++){
            DEAL_ALL_SOLE_NUM.get(code).add(records.get(i).getVOLUME_INC());
            //大额成交
            this.turnoverIncBig(code,records.get(i));
            String realTimeVar = records.get(i).getDATE_STR();
            realTimeVar = realTimeVar.substring(0, realTimeVar.length() - 3);
            if (realTime.equals(realTimeVar)) {
                dealNum.add(records.get(i).getPRICE());
                dealSoleNum.add(records.get(i).getVOLUME_INC());
                continue;
            }
            //每分钟基本信息
            GatherRecord gatherFund = GatherRecord.builder()
                    .stockCode(code)
                    .realTime(realTime)
                    .price(dealNum.get(0))
                    .dealNum(dealNum.size())
                    .dealSoleNum(dealSoleNum.size())
                    .build();
            gatherFunds.add(gatherFund);
            //时间重置
            realTime = realTimeVar;
            //清空上一分钟每笔成交量
            dealNum.clear();
            dealNum.add(records.get(i).getPRICE());
            dealSoleNum.clear();
            dealSoleNum.add(records.get(i).getVOLUME_INC());
        }
        return gatherFunds;
    }

    /**
     * 大额成交
     */
    public void turnoverIncBig(String code,FundRecord record){
        if(Double.valueOf(record.getTURNOVER_INC()) >= 1000000){
            redisTemplate.opsForList().rightPushAll("turnover-inc-big"+code, JSON.toJSONString(record));
        }
    }

    /**
     * 上午基本信息
     */
    public void saveFundRealAm(FundReal fundReal){
        fundService.saveFund(fundReal);
    }

    /**
     * 每日指标
     */
    public void saveDailyBasic(Map<String,Object> dailyBasic) {
        fundService.saveDailyBasic(dailyBasic);
    }

    public static int getDealAllNum(String code){
        if(!DEAL_ALL_NUM.containsKey(code)){
            return 0;
        }
        return DEAL_ALL_NUM.get(code);
    }

    public static int getDealAllSoleNum(String code){
        return DEAL_ALL_SOLE_NUM.get(code).size();
    }

    public static void clearCache(){
        OPEN_PRICES.clear();
        DEAL_ALL_NUM.clear();
        DEAL_ALL_SOLE_NUM.clear();
    }
}
