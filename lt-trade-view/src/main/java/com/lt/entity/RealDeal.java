package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/5/13
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealDeal {
    private String code;
    private String recordStrs;
}
