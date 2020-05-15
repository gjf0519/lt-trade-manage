package com.lt.service;

import com.lt.common.page.PageParams;
import com.lt.entity.DealRecord;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.entity.GatherRecord;
import com.lt.mapper.FundMapper;
import com.lt.vo.DailyBasicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description
 * @date 2020/3/24
 */
@Slf4j
@Service
public class FundService {

    @Autowired
    FundMapper fundMapper;

    public int getFundCount() {
        return fundMapper.getFundCount();
    }

    public List<FundEntity> getFundList(PageParams pageParams) {
        return fundMapper.getFundList(pageParams);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDailyBasic(Map<String, Object> result){
        fundMapper.saveDailyBasic(result);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDailyBasics(List<Map<String, Object>> results){
        fundMapper.saveDailyBasics(results);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFund(FundReal fundReal){
        fundMapper.saveFund(fundReal);
    }

    public List<DailyBasicVo> queryByStockCode(String code, String tradeDate){
        return fundMapper.queryByStockCode(code,tradeDate);
    }

    public List<FundReal> selectByTarget(String date,String lastDate) {
        return fundMapper.selectByTarget(date,lastDate);
    }

    public void updateFivePctChg(String code, double five_pct_chg, String tradeDate) {
        fundMapper.updateFivePctChg(code,  five_pct_chg, tradeDate);
    }

    public Integer queryDealNum(String code, String createTime) {
        return fundMapper.queryDealNum(code,createTime);
    }

    public void updateFiveVolumeRatio(String code, double five_volume_ratio, String createTime) {
        fundMapper.updateFiveVolumeRatio(code,  five_volume_ratio, createTime);
    }

    public List<String> queryDailyBasic(String createTime) {
        return fundMapper.queryDailyBasic(createTime);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveGatherRecord(List<GatherRecord> records) {
        fundMapper.saveGatherRecord(records);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveStatDeal(DealRecord dealRecord) {
        fundMapper.saveStatDeal(dealRecord);
    }
}
