package com.lt.chart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaijf
 * @description 柱状图
 * @date 2020/3/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedoAllPctPillar {
    private List<String> dimensions;
    private List<?> source;
}
