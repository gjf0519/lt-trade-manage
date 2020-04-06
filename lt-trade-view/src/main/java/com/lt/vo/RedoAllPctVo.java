package com.lt.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/3/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedoAllPctVo {
    private String stockCode;
    private String redoAllPct;
    private String emphasis;
}
