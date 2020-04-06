package com.lt.service;

import com.lt.entity.DailyBasic;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.mapper.FundMapper;
import com.lt.task.RealFundExtract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@Service
public class FundService {

    @Autowired
    FundMapper fundMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveFund(FundReal fundReal){
        fundMapper.saveFund(fundReal);
    }

    /**
     * 更新数据重复占比
     * @param fundEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRepetitive(FundEntity fundEntity) {
        fundMapper.saveRepetitive(fundEntity);
    }

    public List<DailyBasic> queryByStockCode(String code){
        return fundMapper.queryByStockCode(code);
    }

    public List<FundReal> selectByTarget(String date) {
        return fundMapper.selectByTarget(date);
    }

    public List<String> selectFundRedo(String codes,double pct) {
        return fundMapper.selectFundRedo(codes,pct);
    }

    public void updateDealNum(FundEntity fundEntity) {
        fundMapper.updateDealNum(fundEntity);
    }

    public void updateClinchChangeMinute(FundEntity fundEntity) {
        fundMapper.updateClinchChangeMinute(fundEntity);
    }

    public void updateOpen(String key, RealFundExtract.RealMarket value) {
        fundMapper.updateOpen(key,value);
    }
}
