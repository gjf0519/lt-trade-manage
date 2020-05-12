package com.lt.controller;

import com.lt.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaijf
 * @description
 * @date 2020/2/16
 */
@Slf4j
@RestController
@RequestMapping("hello")
public class HelloController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    HelloService helloService;

    @GetMapping("/{msg}")
    public String hello(@PathVariable String msg){
        return helloService.hello(msg)+":"+serverPort;
    }

    @GetMapping
    public String hello2(String msg){
        System.out.println("================"+msg);
        return helloService.hello(msg)+":"+serverPort;
    }

    @GetMapping("/sen")
    public String hello2Sen(){
        return helloService.hello("sen")+":"+serverPort;
    }

}
