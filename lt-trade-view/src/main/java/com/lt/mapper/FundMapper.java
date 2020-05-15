package com.lt.mapper;

import com.lt.common.page.PageParams;
import com.lt.entity.*;
import com.lt.vo.DailyBasicVo;
import com.lt.vo.RedoAllPctVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author gaijf
 * @description
 * @date 2020/3/24
 */
@Mapper
public interface FundMapper {
    int getFundCount();

    List<FundEntity> getFundList(@Param("pageParams") PageParams pageParams);

    void saveDailyBasic(Map<String, Object> result);

    void saveDailyBasics(List<Map<String, Object>> results);

    @Insert({"insert into lt_fund_real (stock_code,open_price,makers_fund_in,makers_fund_out,makers_in_flow" +
            ",retail_funt_in,retail_funt_out,retail_in_flow,amounts,pct_chg,exchange,create_time) values (#{stockCode},#{openPrice},#{makersFundIn}," +
            "#{makersFundOut},#{makersInFlow},#{retailFuntIn},#{retailFuntOut},#{retailInFlow},#{amounts},#{pctChg},#{exchange},#{createTime})"})
    void saveFund(FundReal fundReal);

    @Select({"select t.close,t.amount,t.trade_date,t.pct_chg,t.free_share from lt_daily_basic " +
            "t where t.ts_code = #{code} and t.trade_date < #{tradeDate} ORDER BY t.trade_date desc LIMIT 5"})
    List<DailyBasicVo> queryByStockCode(@Param("code") String code, @Param("tradeDate") String tradeDate);

    @Select({"SELECT r.* from (  " +
            "  SELECT m.*,c.pct_chg dpct_chg,c.circ_mv,c.five_pct_chg,(m.amounts/c.circ_mv) circ_mv_rideo FROM (  " +
            "    SELECT u.* from (  " +
            "      SELECT t.stock_code,t.pct_chg,(t.makers_in_flow+t.retail_in_flow) flow,t.create_time,t.exchange,  " +
            "        t.open_price,f.repetition_am_pct,f.five_volume_ratio,f.clinch_change_minute,t.amounts from lt_fund_real t  " +
            "        left JOIN lt_deal_record f on f.stock_code = t.stock_code and f.create_time = #{date}  " +
            "          " +
            "    ) u where u.flow >= -0.01 and u.flow <= 0.00 and u.pct_chg < 2 and u.pct_chg > -3   " +
            "      and u.exchange > 0.1 AND u.exchange < 3 and u.repetition_am_pct >= 0.68  and u.five_volume_ratio > 1 and u.create_time = #{date} ) m  " +
            "  LEFT  JOIN  " +
            "   lt_daily_basic c ON c.ts_code = m.stock_code AND c.trade_date = #{lastDate}  " +
            ") r where r.dpct_chg < 2 and r.five_pct_chg <= 0.0 and r.circ_mv < 2000000.0000 and r.circ_mv_rideo > 0.002 ORDER BY r.circ_mv"})
    List<FundReal> selectByTarget(@Param("date") String date,@Param("lastDate") String lastDate);

    @Update({"update lt_daily_basic set five_pct_chg=#{five_pct_chg} where ts_code=#{code} and trade_date=#{tradeDate}"})
    void updateFivePctChg(@Param("code") String code,@Param("five_pct_chg") double five_pct_chg,@Param("tradeDate") String tradeDate);


    @Select({"select sum(t.deal_all_num) from lt_deal_record t " +
            " where t.stock_code = #{code} and t.create_time <= #{createTime} ORDER BY t.create_time desc LIMIT 5"})
    Integer queryDealNum(@Param("code") String code, @Param("createTime") String createTime);

    @Update({"update lt_deal_record set five_volume_ratio=#{five_volume_ratio} where stock_code=#{code} and create_time=#{createTime}"})
    void updateFiveVolumeRatio(@Param("code") String code,@Param("five_volume_ratio") double five_volume_ratio,@Param("createTime") String createTime);

    @Select({"select ts_code from lt_daily_basic where trade_date=#{createTime}"})
    List<String> queryDailyBasic(String createTime);

    @Insert({"insert into lt_deal_record (stock_code,repetition_pct,deal_all_num,deal_all_sole_num,five_volume_ratio,create_time) " +
            "values (#{stockCode},#{repetitionPct},#{dealAllNum},#{dealAllSoleNum},#{fiveVolumeRatio},#{createTime})"})
    void saveStatDeal(DealRecord dealRecord);

    void saveGatherRecord(List<GatherRecord> records);
}
