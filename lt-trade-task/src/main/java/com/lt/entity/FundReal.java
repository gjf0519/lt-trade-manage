package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/3/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundReal {
    private String stockCode;
    //主力流入
    private double makersFundIn;
    //主力流出
    private double makersFundOut;
    //主力净流入
    private double makersInFlow;
    //散户流入
    private double retailFuntIn;
    //散户流出
    private double retailFuntOut;
    //散户净流入
    private double retailInFlow;
    //资金净流入
    private double flowIn;
    //交易总金额
    private double amounts;
    //涨跌幅
    private double pctChg;
    //换手率
    private double exchange;
    //是否量比放大
    private double openPrice;
    private String createTime;
}
