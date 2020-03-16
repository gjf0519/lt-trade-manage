package com.lt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Slf4j
@Controller
public class SysController {

//    @RequestMapping("index")
//    public String index(HttpServletRequest request, HttpServletResponse response){
//        System.out.println("登录首页==============");
//        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return "index";
//    }
}
