package com.lt.service;

import com.lt.entity.ltDepartment;
import com.lt.mapper.DepartmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/23
 */
@Slf4j
@Service
public class DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    public List<ltDepartment> getDepartments(){
        return departmentMapper.getDepartments();
    };
}
