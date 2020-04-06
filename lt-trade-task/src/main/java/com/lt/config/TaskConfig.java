package com.lt.config;

import com.lt.task.*;
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
     * 上午成交明细
     * @return
     */
    @Bean
    public RealFundDetailAm realFundDetailAm(){
        RealFundDetailAm realFundDetailAm = new RealFundDetailAm();
        return realFundDetailAm;
    }

    /**
     * 当前成交明细
     * @return
     */
    @Bean
    public RealFundDetailAll realFundDetailAll(){
        RealFundDetailAll realFundDetailAll = new RealFundDetailAll();
        return realFundDetailAll;
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

}
