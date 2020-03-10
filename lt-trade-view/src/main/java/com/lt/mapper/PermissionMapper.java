package com.lt.mapper;

import com.lt.entity.LtPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Mapper
public interface PermissionMapper {
    List<LtPermission> listByRoleId(int id);
}
