package com.lt.mapper;

import com.lt.entity.LtUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    LtUser queryUsers();

    @Select({"select t.* from lt_user t where t.user_name = #{userName}"})
    LtUser loadUserByUsername(@Param("userName") String userName);
}
