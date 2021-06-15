package com.ruyuan2020.little.project.rocketmq.api.login.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 登录的rocketmq生产者配置
 */
@Configuration
public class LoginProducerConfiguration {

    /**
     * NameServer服务器地址
     */
    @Value("${rocketmq.namesrv.address}")
    private String namesrvAddress;
    /**
     * 生产者组
     */
    @Value("${rocketmq.login.producer.group}")
    private String loginProducerGroup;

    @Bean
    public DefaultMQProducer loginMQProducer() throws MQClientException {
        DefaultMQProducer loginMQProducer = new DefaultMQProducer(loginProducerGroup);
        loginMQProducer.setNamesrvAddr(namesrvAddress);
        // 启动登录消息生产者
        loginMQProducer.start();
        return loginMQProducer;
    }
}
