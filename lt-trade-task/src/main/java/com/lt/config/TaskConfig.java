package com.lt.config;

import com.lt.task.DailyBasicExtract;
import com.lt.task.DownLoadGif;
import com.lt.task.RealDealRecord;
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
     * 每日指标
     * @return
     */
    @Bean
    public DailyBasicExtract dailyBasicExtract(){
        DailyBasicExtract dailyBasicExtract = new DailyBasicExtract();
        return dailyBasicExtract;
    }

    /**
     * 日线图片下载
     * @return
     */
    @Bean
    public DownLoadGif downLoadGif(){
        DownLoadGif downLoadGif = new DownLoadGif();
        return downLoadGif;
    }

    @Bean
    public RealDealRecord realFundDetail163Am(){
        RealDealRecord realDealRecord = new RealDealRecord();
        return realDealRecord;
    }

}
