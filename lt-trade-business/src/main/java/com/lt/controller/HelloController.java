package com.lt.controller;

import com.lt.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaijf
 * @description
 * @date 2020/2/16
 */
@Slf4j
@RestController
public class HelloController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    HelloService helloService;

    @GetMapping("hello/{msg}")
    public String hello(@PathVariable String msg){
        return helloService.hello(msg)+":"+serverPort;
    }

    @GetMapping("hello2/{msg}")
    public String hello2(@PathVariable String msg){
        return helloService.hello(msg)+":"+serverPort;
    }

    @GetMapping("hello2/sen/{msg}")
    public String hello2Sen(@PathVariable String msg){
        return helloService.hello(msg)+":"+serverPort;
    }

    @GetMapping("hello3")
    public String hello3(){
        return helloService.hello("hello3")+":"+serverPort;
    }
}
