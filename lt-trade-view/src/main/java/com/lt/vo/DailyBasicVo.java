package com.lt.vo;

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
public class DailyBasicVo {
    private String tsCode;//TS股票代码
    private String tradeDate;//交易日期
    private double close;
    private double turnoverRate;//换手率（%）
    private double turnoverRateF;//换手率（自由流通股）
    private double volumeRatio;//量比
    private double ps;//市销率
    private double psTtm;//市销率（TTM）
    private double totalShare;//总股本 （万股）
    private double floatShare;//流通股本 （万股）
    private double freeShare;//自由流通股本 （万）
    private double totalMv;//总市值 （万元）
    private double circMv;//流通市值（万元）
    private double pctChg;//涨跌幅
    private double amount; //交易金额
}
