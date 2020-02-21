package com.lt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author gaijf
 * @description
 * @date 2020/2/15
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class TradeBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeBusinessApplication.class,args);
    }
}
