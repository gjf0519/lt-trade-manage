package com.lt.route;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

/**
 * @author gaijf
 * @description 路由缓存对象
 * @date 2020/2/19
 */
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private final Map<String, RouteDefinition> routes = synchronizedMap(
            new LinkedHashMap<String, RouteDefinition>());

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            if (StringUtils.isEmpty(r.getId())) {
                return Mono.error(new IllegalArgumentException("id may not be empty"));
            }
            routes.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(
                    new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

    public Mono<Void> deleteAll() {
        routes.clear();
        return Mono.empty();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }
}
