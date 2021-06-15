package com.ruyuan2020.little.project.rocketmq.api.order.consumer;

import com.ruyuan2020.little.project.rocketmq.api.order.listener.OrderDelayMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单延时消息消费者消息
 */
@Configuration
public class OrderDelayConsumerConfiguration {

    @Value("${rocketmq.namesrv.address}")
    private String namesrvAddress;

    @Value("${rocketmq.order.delay.consumer.group}")
    private String orderDelayConsumerGroup;

    @Value("${rocketmq.order.delay.topic}")
    private String orderDelayTopic;

    @Bean
    public DefaultMQPushConsumer orderDelayMqConsumer(
        @Qualifier(value = "orderDelayMessageListener") OrderDelayMessageListener orderDelayMessageListener)
        throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(orderDelayConsumerGroup);
        consumer.setNamesrvAddr(namesrvAddress);
        consumer.setMessageListener(orderDelayMessageListener);
        consumer.subscribe(orderDelayTopic, "*");
        consumer.start();
        return consumer;
    }
}
