package com.lt.config;

import com.lt.task.ClinchExtract;
import com.lt.task.RealFundExtract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @author gaijf
 * @description
 * @date 2019/9/17
 */
@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {

    /**
     * 设置线程池类型，默认是单线程池执行
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(2));
    }

    /**
     * 实时资金抽取
     * @return
     */
    @Bean
    public RealFundExtract realFundExtract(){
        RealFundExtract realFundExtract = new RealFundExtract();
        return realFundExtract;
    }

    /**
     * 成交明细
     * @return
     */
    @Bean
    public ClinchExtract clinchExtract(){
        ClinchExtract clinchExtract = new ClinchExtract();
        return clinchExtract;
    }
}
