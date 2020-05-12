package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/4/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatherFund {
    private String pchChg;
    private int dealSoleNum;
    private int dealNum;
}
