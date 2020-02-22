package com.lt.config;

import com.lt.route.DynamicRouteExecute;
import com.lt.route.NacosGateWayRefresher;
import com.lt.route.NacosRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public NacosGateWayRefresher nacosGateWayRefresher(
            @Value("${route.config.file-name}") String routeConfigFileName,
            @Value("${route.config.file-group}") String routeConfigFileGroup){
        return new NacosGateWayRefresher(routeConfigFileName,routeConfigFileGroup);
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
