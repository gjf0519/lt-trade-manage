package com.lt.mq;

import com.lt.service.TransformService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.support.RocketMQConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gaijf
 * @description 每日指标
 * @date 2020/5/14
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "TUSHARE-DAILY-BASIC", consumerGroup = "BASIC-CONSUMER-GROUP")
public class BasicConsumerListener implements RocketMQListener<MessageExt>,RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {
    @Autowired
    TransformService transformService;

    @Override
    public void onMessage(MessageExt ext) {
//        transformService.saveDailyBasic(dailyBasic);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        //批量消费
        //consumer.setConsumeMessageBatchMaxSize(10);
        //重试次数、默认16次
        consumer.setMaxReconsumeTimes(2);
    }
}
