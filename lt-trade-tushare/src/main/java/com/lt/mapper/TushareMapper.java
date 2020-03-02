package com.lt.mapper;

import org.apache.ibatis.annotations.Mapper;

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
}
