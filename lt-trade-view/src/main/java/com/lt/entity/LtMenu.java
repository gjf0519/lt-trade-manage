package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LtMenu {
    private int id;
    private String title;
    private String icon;
    private String href;
    private String spread;
    private String pid;
    private List<LtMenu> children;
}
