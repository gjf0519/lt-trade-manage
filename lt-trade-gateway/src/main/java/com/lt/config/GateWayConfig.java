package com.lt.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.lt.route.DynamicRouteExecute;
import com.lt.route.NacosGateWayRefresher;
import com.lt.route.NacosRouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * @author gaijf
 * @description
 * @date 2020/2/19
 */
@Configuration
public class GateWayConfig {

    /**
     * nacos动态获取路由配置信息
     * @return
     */
    @Bean
    public NacosGateWayRefresher nacosGateWayRefresher(){
        return new NacosGateWayRefresher();
    }

    /**
     * 自定义路由缓存容器
     * @return
     */
    @Bean
    public NacosRouteDefinitionRepository nacosRouteDefinitionRepository(){
        return new NacosRouteDefinitionRepository();
    }

    /**
     * 执行路由增删改查操作
     * @return
     */
    @Bean
    public DynamicRouteExecute dynamicRouteExecute(){
        return new DynamicRouteExecute();
    }

    /**
     * sentinel自定义返回异常
     * @return
     */
//    @Bean
//    public BlockRequestHandler blockRequestHandler() {
//        return (exchange, t) -> ServerResponse.status(444).contentType(MediaType.APPLICATION_JSON)
//                .body(fromValue("SCS Sentinel block"));
//    }
}
