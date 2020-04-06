package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundEntity {

    private String stockCode;
    //成交订单上午重复占比
    private double repetitionAmPct;
    //成交订单当天重复占比
    private double redoAllPct;
    //成交数量
    private int dealNum;
    //每分钟价格变动
    private String clinchChangeMinute;
    private String createTime;
}
