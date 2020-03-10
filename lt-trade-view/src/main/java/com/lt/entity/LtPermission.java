package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LtPermission {
    private String id;
    private String resource;
}
