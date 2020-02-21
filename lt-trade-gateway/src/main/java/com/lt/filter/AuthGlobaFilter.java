package com.lt.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

/**
 * @author gaijf
 * @description
 * @date 2020/2/19
 */
@Component
public class AuthGlobaFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("token");
        String uri = exchange.getRequest().getURI().getPath();
        if(true){
            return chain.filter(exchange);
        }
        return exchange.getResponse().setComplete();
    }
}
