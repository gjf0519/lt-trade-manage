package com.lt.mapper;

import com.lt.entity.ltDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/23
 */
@Mapper
public interface DepartmentMapper {

    @Select("select * from lt_department")
    List<ltDepartment> getDepartments();
}
