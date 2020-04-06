package com.lt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.entity.ltDepartment;
import com.lt.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/3/23
 */
@Slf4j
@Controller
@RequestMapping("depart")
public class DepartController {

    @Autowired
    DepartmentService departmentService;

    @RequestMapping("/main")
    public String toMain(){
        return "system/depart/departMain";
    }

    @RequestMapping("/main_left")
    public String toLeft(){
        return "system/depart/departLeft";
    }

    @RequestMapping("/main_right")
    public String toRight(){
        return "system/depart/departRight";
    }

    @RequestMapping("/departments")
    @ResponseBody
    public JSONObject getDepartments(){
        List<ltDepartment> result = departmentService.getDepartments();
        JSONObject obj = new JSONObject();
        obj.put("code","0");
        obj.put("msg","操作成功");
        obj.put("data",result);
        return obj;
    }
}
