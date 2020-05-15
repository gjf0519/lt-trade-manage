package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description 每分钟成交明细指标
 * @date 2020/5/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatherRecord {
    //股票代码
    private String stockCode;
    //成交时间
    private String realTime;
    //当前价格
    private String price;
    //涨幅
    private double pchChg;
    //每分钟全部成交量
    private int dealNum;
    //每分钟去重成交量
    private int dealSoleNum;
}
