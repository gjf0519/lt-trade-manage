package com.lt.mapper;

import com.lt.entity.LtPermission;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
public interface PermissionMapper {
    List<LtPermission> listByRoleId(int id);
}
