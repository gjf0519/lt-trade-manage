package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/4/14
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundRecord {
    //成交性质1买0中性-1卖
    private String TRADE_TYPE;
    //上次成交价
    private String PRICE_PRE;
    //成交手数
    private String VOLUME_INC;
    //当前价格
    private String PRICE;
    //成交金额
    private String TURNOVER_INC;
    //价格变动
    private String PRICE_INC;
    //成交时间
    private String DATE_STR;
    private String TRADE_TYPE_STR;
}
