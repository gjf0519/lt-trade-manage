package com.lt.service;

import com.lt.entity.FundEntity;
import com.lt.mapper.FundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveFund(FundEntity fundEntity){
        fundMapper.saveFund(fundEntity);
    }

    /**
     * 更新数据重复占比
     * @param fundEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void updRepetitive(FundEntity fundEntity) {
        fundMapper.updRepetitive(fundEntity);
    }
}
