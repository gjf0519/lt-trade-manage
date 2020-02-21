package com.lt.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractSharedListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gaijf
 * @description 通过nacos注册中心获取路由信息
 * @date 2020/2/17
 */
@Slf4j
public class NacosGateWayRefresher implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private NacosConfigManager nacosConfigManager;

	@Autowired
	private NacosConfigProperties nacosConfigProperties;

	private final boolean isRefreshEnabled = true;

	private ConfigService configService;

	private AtomicBoolean ready = new AtomicBoolean(false);

	private Map<String, Listener> listenerMap = new ConcurrentHashMap<>(16);

	@Autowired
	DynamicRouteExecute dynamicRouteExecute;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		configService = nacosConfigManager.getConfigService();
		try {
			String cotent = configService.getConfig("lt-trade-gateway.yaml","DEFAULT_GROUP",5000);
			dynamicRouteExecute.add(cotent);
		} catch (NacosException e) {
			e.printStackTrace();
		}
		if (this.ready.compareAndSet(false, true)) {
			this.registerNacosListenersForApplications();
		}
	}

	/**
	 * register Nacos Listeners.
	 */
	private void registerNacosListenersForApplications() {
		if (isRefreshEnabled()) {
			for (NacosPropertySource propertySource : NacosPropertySourceRepository
					.getAll()) {
				if (!propertySource.isRefreshable()) {
					continue;
				}
				String dataId = propertySource.getDataId();
				registerNacosListener(propertySource.getGroup(), dataId);
			}
		}
	}

	private void registerNacosListener(final String groupKey, final String dataKey) {
		String key = NacosPropertySourceRepository.getMapKey(dataKey, groupKey);
		Listener listener = listenerMap.computeIfAbsent(key,
				lst -> new AbstractSharedListener() {
					@Override
					public void innerReceive(String dataId, String group,
							String configInfo) {
						dynamicRouteExecute.update(configInfo);
					}
				});
		try {
			configService.addListener(dataKey, groupKey, listener);
		}
		catch (NacosException e) {
			log.warn(String.format(
					"register fail for nacos listener ,dataId=[%s],group=[%s]", dataKey,
					groupKey), e);
		}
	}

	public boolean isRefreshEnabled() {
		if (null == nacosConfigProperties) {
			return isRefreshEnabled;
		}
		// Compatible with older configurations
		if (nacosConfigProperties.isRefreshEnabled() && !isRefreshEnabled) {
			return false;
		}
		return isRefreshEnabled;
	}
}
