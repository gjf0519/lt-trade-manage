package com.lt.config;

import feign.Contract;
import feign.Logger;
import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaijf
 * @description
 * @date 2020/1/22
 */
@Configuration
public class FeignConfig {

    /**
     * 日志级别配置
     *
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        /**
         * NONE：不输出日志。
         * BASIC：只输出请求方法的 URL 和响应的状态码以及接口执行的时间。
         * HEADERS：将 BASIC 信息和请求头信息输出。
         * FULL：输出完整的请求信息。
         */
        return Logger.Level.FULL;
    }

    /**
     * 协议规则
     * @return
     */
    @Bean
    public Contract feignContract() {
        /**
         * feign.Contract.Default()
         * SpringMvcContract()
         * HystrixDelegatingContract()
         */
        return new SpringMvcContract();
    }

    /**
     * Basic 认证配置
     * @return
     */
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("user", "password");
//    }

    /**
     * 超时时间配置
     * @return
     */
    @Bean
    public Request.Options options() {
        /**
         * 第一个参数是连接超时时间（ms），默认值是 10×1000；
         * 第二个是取超时时间（ms），默认值是 60×1000。
         */
        return new Request.Options(5000, 10000);
    }

//    Feign 中提供了自定义的编码解码器设置，同时也提供了多种编码器的实现，比如 Gson、Jaxb、Jackson。我们可以用不同的编码解码器来处理数据的传输。
//    如果你想传输 XML 格式的数据，可以自定义 XML 编码解码器来实现获取使用官方提供的 Jaxb。
    /**
     * 编码
     * @return
     */
    @Bean
    public Decoder decoder() {
        return new Decoder.Default();
    }

    /**
     * 解码
     * @return
     */
    @Bean
    public Encoder encoder() {
        return new Encoder.Default();
    }
}
