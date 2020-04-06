package com.lt.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @JsonIgnore
    private int id;
    private String title;
    private String icon;
    private String href;
    private boolean spread;
    @JsonIgnore
    private int pid;
    private List<LtMenu> children = new ArrayList<>();
}
