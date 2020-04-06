package com.lt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description
 * @date 2020/2/25
 */
@Mapper
public interface TushareMapper {

    void saveStockBasic(List<Map<String,Object>> stocks);

    void saveDailyBasic(List<Map<String, Object>> results);

    @Update({"update lt_daily_basic set close=#{close} where ts_code=#{stockCode} and trade_date=#{tradeDate}"})
    void updateClose(@Param("stockCode") String code,@Param("tradeDate") String tradeDate,@Param("close") String close);
}
