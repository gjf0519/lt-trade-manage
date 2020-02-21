package com.lt.service.fallback;

import com.lt.service.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author gaijf
 * @description
 * @date 2020/2/17
 */
@Component
public class HelloServiceFallback implements HelloService {
    @Override
    public String hello(String msg) {
        return "服务器开小差了！！！";
    }
}
