package com.lt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gaijf
 * @description
 * @date 2020/5/14
 */
@Slf4j
@EnableAsync
@Configuration
public class ExecutePoolConfig implements AsyncConfigurer {
    @Value("${spring.executor.core-pool-size}")
    private int corePoolSize;
    @Value("${spring.executor.max-pool-size}")
    private int maxPoolSize;
    @Value("${spring.executor.keep-alive-seconds}")
    private int keepAliveSeconds;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorInfo();
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //活跃时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //线程名字前缀
        executor.setThreadNamePrefix("NativeExecutor");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    /**
     *  异步任务中异常处理
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (arg0, arg1, arg2) -> {
            log.info("exception method:"+arg1.getName()+"exception:"+arg0.getMessage());
        };
    }
}
