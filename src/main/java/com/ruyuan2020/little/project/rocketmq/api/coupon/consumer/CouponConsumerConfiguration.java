package com.ruyuan2020.little.project.rocketmq.api.coupon.consumer;

import com.ruyuan2020.little.project.rocketmq.api.coupon.listener.FirstLoginMessageListener;
import com.ruyuan2020.little.project.rocketmq.api.coupon.listener.OrderFinishedMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 优惠券模块：消费者配置类
 */
@Configuration
public class CouponConsumerConfiguration {

    /**
     * NameServer的地址
     */
    @Value("${rocketmq.namesrv.address}")
    private String namesrvAddress;

    /**
     * 登录topic
     */
    @Value("${rocketmq.login.topic}")
    private String loginTopic;

    /**
     * 登录消息消费者组
     */
    @Value("${rocketmq.login.consumer.group}")
    private String loginConsumerGroup;


    /**
     * 退房订单topic
     */
    @Value("${rocketmq.order.finished.topic}")
    private String orderFinishedTopic;

    /**
     * 退房订单consumerGroup
     */
    @Value("${rocketmq.order.finished.consumer.group}")
    private String orderFinishedConsumerGroup;

    /**
     * 登录消息的 消费者Bean
     * @return 登录消息的consumer bean
     * @throws MQClientException
     */
    @Bean(value = "loginConsumer")
    public DefaultMQPushConsumer loginConsumer(
        @Qualifier(value = "firstLoginMessageListener") FirstLoginMessageListener listener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(loginConsumerGroup);
        consumer.setNamesrvAddr(namesrvAddress);
        // 设置消费者订阅的topic
        consumer.subscribe(loginTopic, "*");
        consumer.setMessageListener(listener);
        consumer.start();
        return consumer;
    }

    /**
     * 订单退房消息
     *
     * @return 订单退房消息的consumer bean
     */
    @Bean(value = "orderFinishedConsumer")
    public DefaultMQPushConsumer finishedConsumer(@Qualifier(value = "orderFinishedMessageListener")
        OrderFinishedMessageListener orderFinishedMessageListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(orderFinishedConsumerGroup);
        consumer.setNamesrvAddr(namesrvAddress);
        consumer.subscribe(orderFinishedTopic, "*");
        consumer.setMessageListener(orderFinishedMessageListener);
        consumer.start();
        return consumer;
    }
}
