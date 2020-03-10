package com.lt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gaijf
 * @description
 * @date 2020/3/10
 */
@Slf4j
@Controller
public class SysController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
