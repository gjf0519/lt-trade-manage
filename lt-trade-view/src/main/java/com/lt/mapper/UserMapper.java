package com.lt.mapper;

import com.lt.entity.LtUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    LtUser queryUsers();
}
