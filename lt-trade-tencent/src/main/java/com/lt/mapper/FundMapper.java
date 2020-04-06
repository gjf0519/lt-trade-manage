package com.lt.mapper;

import com.lt.entity.DailyBasic;
import com.lt.entity.FundEntity;
import com.lt.entity.FundReal;
import com.lt.task.RealFundExtract;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@Mapper
public interface FundMapper {

    @Insert({"insert into lt_fund_real (stock_code,makers_fund_in,makers_fund_out,makers_in_flow" +
            ",retail_funt_in,retail_funt_out,retail_in_flow,amounts,pct_chg,exchange,isIncrease,create_time) values (#{stockCode},#{makersFundIn}," +
            "#{makersFundOut},#{makersInFlow},#{retailFuntIn},#{retailFuntOut},#{retailInFlow},#{amounts},#{pctChg},#{exchange},#{isIncrease},#{createTime})"})
    void saveFund(FundReal fundReal);

    @Update({"update lt_fund_detail set redo_minute_pct=#{redoMinutePct},redo_all_pct=#{redoAllPct},repetition_am_pct=#{repetitionAmPct} " +
            "where stock_code=#{stockCode} and create_time=#{createTime}"})
    void updRepetitive(FundEntity fundEntity);

    @Update({"insert into lt_fund_detail (stock_code,redo_all_pct,repetition_am_pct,deal_num,clinch_change_minute,create_time)" +
            "values (#{stockCode},#{redoAllPct},#{repetitionAmPct},#{dealNum},#{clinchChangeMinute},#{createTime}) "})
    void saveRepetitive(FundEntity fundEntity);

    @Select({"select t.amount,t.trade_date,t.pct_chg,t.free_share from lt_daily_basic " +
            "t where t.ts_code like concat('%',#{code}) and t.trade_date != '2020-04-01' ORDER BY t.trade_date desc LIMIT 5"})
    List<DailyBasic> queryByStockCode(@Param("code") String code);

    @Select({"SELECT u.stock_code stockCode,u.amounts from (" +
            "SELECT t.stock_code,t.pct_chg,(t.makers_in_flow+t.retail_in_flow) flow_in,t.amounts  from lt_fund_real t" +
            " where t.create_time=#{date}) u where u.flow_in >= -0.01 and u.pct_chg >-3 and u.pct_chg < 2"})
    List<FundReal> selectByTarget(@Param("date") String date);

    @Select({"select t.stock_code from lt_fund_detail t where t.stock_code in (${codes}) and t.repetition_am_pct >=#{pct}"})
    List<String> selectFundRedo(@Param("codes") String codes,@Param("pct") double pct);

    @Update({"update lt_fund_detail set deal_num=#{dealNum} where stock_code=#{stockCode} and create_time=#{createTime}"})
    void updateDealNum(FundEntity fundEntity);

    @Update({"update lt_fund_detail set clinch_change_minute=#{clinchChangeMinute} where stock_code=#{stockCode} and create_time=#{createTime}"})
    void updateClinchChangeMinute(FundEntity fundEntity);

    @Update({"update lt_fund_real set open_price=#{item.openPrice} where stock_code like concat('%',#{stockCode}) and create_time='2020-04-03' "})
    void updateOpen(@Param("stockCode") String key,@Param("item") RealFundExtract.RealMarket value);
}
