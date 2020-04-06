package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/4/3
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundDetail {
    private int id;
    private String stockCode;
    private double redoMinutePct;
    private double repetitionAmPct;
    private String redoAllPct;
    private int dealNum;
    private String createTime;
}
