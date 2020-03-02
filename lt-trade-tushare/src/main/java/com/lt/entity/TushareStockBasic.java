package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2019/11/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TushareStockBasic {
    /**
     * TS代码
     */
    private String tsCode;
    /**
     *股票代码
     */
    private String symbol;
    /**
     *股票名称
     */
    private String name;
    /**
     *所在地域
     */
    private String area;
    /**
     *所属行业
     */
    private String industry;
    /**
     *市场类型
     */
    private String market;
    /**
     *上市状态： L上市 D退市 P暂停上市
     */
    private String listStatus;
    /**
     *是否沪深港通标的，N否 H沪股通 S深股通
     */
    private String isHs;
}
