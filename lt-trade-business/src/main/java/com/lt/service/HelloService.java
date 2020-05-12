package com.lt.service;

import com.lt.config.FeignConfig;
import com.lt.service.fallback.HelloServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author gaijf
 * @description
 * @date 2020/2/16
 */
@FeignClient(value = "lt-trade-tushare",
        configuration = FeignConfig.class,
        fallback = HelloServiceFallback.class)
public interface HelloService {

    @GetMapping("hello/{msg}")
    String hello(String msg);
}
