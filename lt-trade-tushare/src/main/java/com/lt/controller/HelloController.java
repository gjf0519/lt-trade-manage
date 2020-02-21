package com.lt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaijf
 * @description
 * @date 2020/2/16
 */
@RestController
public class HelloController {

    @GetMapping("hello/{msg}")
    public String Hello(@PathVariable("msg") String msg){
        return "Hello:"+msg;
    }
}
