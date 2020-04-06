package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description Tushare 网站每日指标
 * @date 2019/11/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBasic {
    private String ts_code;//TS股票代码
    private String trade_date;//交易日期
    private String close;
    private double turnover_rate;//换手率（%）
    private double turnover_rate_f;//换手率（自由流通股）
    private double volume_ratio;//量比
    private double ps;//市销率
    private double ps_ttm;//市销率（TTM）
    private double total_share;//总股本 （万股）
    private double float_share;//流通股本 （万股）
    private double free_share;//自由流通股本 （万）
    private double total_mv;//总市值 （万元）
    private double circ_mv;//流通市值（万元）
    private double pct_chg;//涨跌幅
    private double amount; //交易金额
}
