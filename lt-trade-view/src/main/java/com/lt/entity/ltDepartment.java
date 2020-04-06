package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/3/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ltDepartment {
    private String id;
    private String title;
    private String parentId;
}
