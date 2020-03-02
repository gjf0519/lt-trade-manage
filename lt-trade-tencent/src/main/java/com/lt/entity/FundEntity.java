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
    //交易总金额
    private double amounts;
    //成交订单重复占比
    private String redoMinutePct;
    //成交订单重复占比
    private double redoAllPct;
    private String createTime;
}
