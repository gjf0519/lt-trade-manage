package com.lt.mapper;

import com.lt.entity.FundEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@Mapper
public interface FundMapper {

    @Insert({"insert into lt_fund_detail (stock_code,makers_fund_in,makers_fund_out,makers_in_flow" +
            ",retail_funt_in,retail_funt_out,retail_in_flow,amounts,create_time) values (#{stockCode},#{makersFundIn}," +
            "#{makersFundOut},#{makersInFlow},#{retailFuntIn},#{retailFuntOut},#{retailInFlow},#{amounts},#{createTime})"})
    void saveFund(FundEntity fundEntity);

    @Update({"update lt_fund_detail set redo_minute_pct=#{redoMinutePct},redo_all_pct=#{redoAllPct} " +
            "where stock_code=#{stockCode} and create_time=#{createTime}"})
    void updRepetitive(FundEntity fundEntity);
}
