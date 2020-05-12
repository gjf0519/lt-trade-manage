package com.lt.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gaijf
 * @description
 * @date 2019/9/19
 */
@Configuration
public class RestConfig {

    private static final int corePoolSize = 2;
    private static final int maximumPoolSize = 8;
    private static final long keepAliveTime = 2;
    private static final int capacity = 50;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(
                corePoolSize,maximumPoolSize,keepAliveTime,
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(capacity));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    /**
     * 可以支持的客户端
     * 基于 JDK HttpURLConnection 的 SimpleClientHttpRequestFactory，默认。
     * 基于 Apache HttpComponents Client 的 HttpComponentsClientHttpRequestFactory
     * 基于 OkHttp3的OkHttpClientHttpRequestFactory。
     * 基于 Netty4 的 Netty4ClientHttpRequestFactory。
     * 其中HttpURLConnection 和 HttpClient 为原生的网络访问类，OkHttp3采用了 OkHttp3的框架，Netty4 采用了Netty框架。
     * @return
     */
    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    /**
     * 短连接方式
     * @return
     */
    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultMaxPerRoute(50);
        connectionManager.setValidateAfterInactivity(2000);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(100000) //服务器返回数据(response)的时间，超过抛出read timeout
                .setConnectTimeout(5000) //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectionRequestTimeout(1000)//从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)//多线程时使用 设置成共享线程池
                .build();
    }

    /**
     * 长连接方式
     * @return
     */
    @Bean
    public HttpClient httpClientKeep() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultMaxPerRoute(50);
        connectionManager.setValidateAfterInactivity(2000);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(100000) //服务器返回数据(response)的时间，超过抛出read timeout
                .setConnectTimeout(5000) //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectionRequestTimeout(1000)//从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .build();
        ConnectionKeepAliveStrategy strategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, org.apache.http.protocol.HttpContext httpContext) {
                HeaderElementIterator it = new BasicHeaderElementIterator
                        (httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase
                            ("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                return 60 * 1000;//如果没有约定，则默认定义时长为60s
            }
        };
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(strategy)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManagerShared(true)//多线程时使用 设置成共享线程池
                .build();
    }
}
