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
public class TradeTushareApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeTushareApplication.class,args);
    }
}
