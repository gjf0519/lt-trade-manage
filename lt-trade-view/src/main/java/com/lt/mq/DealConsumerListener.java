package com.lt.mq;

import com.lt.entity.RealDeal;
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
 * @description 实时成交明细
 * @date 2020/5/13
 */
@Slf4j
@Component
//@RocketMQMessageListener(topic = "WY-DEAL-RECORD", consumerGroup = "DEAL-CONSUMER-GROUP")
@RocketMQMessageListener(topic = "WY-DEAL-RECORD", consumerGroup = "DEAL-CONSUMER-GROUP-4")
public class DealConsumerListener implements RocketMQListener<RealDeal>,RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {

    @Autowired
    TransformService transformService;

    @Override
    public void onMessage(RealDeal realDeal) {
        transformService.transformDealRecord(realDeal);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        //批量消费
        //consumer.setConsumeMessageBatchMaxSize(10);
        //重试次数、默认16次
        consumer.setMaxReconsumeTimes(2);
    }
}
