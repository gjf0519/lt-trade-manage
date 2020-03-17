package com.lt.mapper;

import com.lt.entity.LtMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/17
 */
@Mapper
public interface MenuMapper {

    @Select({"select * from lt_menu"})
    List<LtMenu> queryAllMenus();
}
