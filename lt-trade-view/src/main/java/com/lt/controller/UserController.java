package com.lt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gaijf
 * @description
 * @date 2020/3/24
 */
@Slf4j
@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("/list")
    public String toListHtml(){
        return "system/user/userList";
    }
}
