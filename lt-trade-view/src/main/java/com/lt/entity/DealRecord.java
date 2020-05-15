package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description 成交记录基本信息
 * @date 2020/2/24
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealRecord {
    private String stockCode;
    //成交订单重复占比
    private double repetitionPct;
    //上午成交数量
    private int dealAllNum;
    //去重成交数量
    private int dealAllSoleNum;
    //成交量与过去5日平均成交量对比
    private double fiveVolumeRatio;
    //每分钟价格变动
    private String clinchChangeMinute;
    private String createTime;
}
