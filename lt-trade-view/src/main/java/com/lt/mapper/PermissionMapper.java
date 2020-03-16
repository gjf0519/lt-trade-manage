package com.lt.mapper;

import com.lt.entity.LtPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Mapper
public interface PermissionMapper {
    @Select({"select p.* from lt_permission p left join lt_role_permission r on r.permission_id = p.id where r.role_id = #{roleId}"})
    List<LtPermission> listByRoleId(@Param("roleId") int id);
}
