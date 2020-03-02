package com.lt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gaijf
 * @description
 * @date 2020/2/16
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TradeTencentApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeTencentApplication.class,args);
    }
}
