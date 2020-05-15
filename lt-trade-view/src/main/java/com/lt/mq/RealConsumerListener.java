package com.lt.mq;

import com.lt.entity.FundReal;
import com.lt.service.TransformService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.support.RocketMQConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gaijf
 * @description 实时股票基本信息
 * @date 2020/5/14
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "REAL-STOCK-INFO", consumerGroup = "REAL-CONSUMER-GROUP")
public class RealConsumerListener implements RocketMQListener<FundReal>,RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {
    @Autowired
    TransformService transformService;

    @Override
    public void onMessage(FundReal fundReal) {
        transformService.saveFundRealAm(fundReal);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        //批量消费
        //consumer.setConsumeMessageBatchMaxSize(10);
        //重试次数、默认16次
        consumer.setMaxReconsumeTimes(2);
    }
}
