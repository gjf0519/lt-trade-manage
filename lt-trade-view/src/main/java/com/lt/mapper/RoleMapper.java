package com.lt.mapper;

import com.lt.entity.LtRole;
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
public interface RoleMapper {

    @Select({"select r.* from lt_role r left join lt_user_role u on u.role_id = r.id where u.user_id = #{userId}"})
    List<LtRole> listByUserId(@Param("userId") int id);
}
