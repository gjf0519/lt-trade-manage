package com.lt.route;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gaijf
 * @description
 * @date 2020/2/17
 */
@Slf4j
public class DynamicRouteExecute implements ApplicationEventPublisherAware {

    @Resource
    private NacosRouteDefinitionRepository nacosRouteDefinitionRepository;
    private ApplicationEventPublisher publisher;

    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 增加路由
     * @param definitionJson
     * @return
     */
    public void add(String definitionJson) {
        List<RouteDefinition> definitions = JSON.parseArray(definitionJson,RouteDefinition.class);
        definitions.stream().forEach(definition -> {
            nacosRouteDefinitionRepository.save(Mono.just(definition)).subscribe();
        });
        notifyChanged();
    }

    /**
     * 更新路由
     * @param definitionJson
     * @return
     */
    public void update(String definitionJson) {
        this.deleteAll();
        List<RouteDefinition> definitions = JSON.parseArray(definitionJson,RouteDefinition.class);
        for(RouteDefinition definition : definitions){
            try {
                nacosRouteDefinitionRepository.save(Mono.just(definition)).subscribe();
                notifyChanged();
            } catch (Exception e) {
                log.info("更新路由异常:{}",e);
            }
        }
    }

    /**
     * 清空路由
     * @return
     */
    public void deleteAll() {
        try {
            this.nacosRouteDefinitionRepository.deleteAll();
            notifyChanged();
        } catch (Exception e) {
            log.info("删除路由异常:{}",e);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
