package com.ruyuan2020.little.project.rocketmq.api.message.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 消费者消息
 */
@Configuration
public class MessageConsumerConfiguration {
    /**
     * namesrv address
     */
    @Value("${rocketmq.namesrv.address}")
    private String namesrvAddress;

    /**
     * 订单topic
     */
    @Value("${rocketmq.order.topic}")
    private String orderTopic;

    /**
     * 订单topic消费者组
     */
    @Value("${rocketmq.order.consumer.group}")
    private String orderConsumerGroup;

    public DefaultMQPushConsumer orderMqConsumer(
        @Qualifier(value = "orderMessageListener") MessageListenerOrderly messageListenerOrderly)
        throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(orderConsumerGroup);
        consumer.setNamesrvAddr(namesrvAddress);
        consumer.subscribe(orderTopic, "*");
        consumer.start();
        return consumer;
    }
}
