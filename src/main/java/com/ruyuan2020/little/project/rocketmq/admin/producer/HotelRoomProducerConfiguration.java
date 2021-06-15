package com.ruyuan2020.little.project.rocketmq.admin.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 酒店客房的rocketmq生产者配置类
 */
@Configuration
public class HotelRoomProducerConfiguration {

    @Value("${rocketmq.namesrv.address}")
    private String namesrvAddress;

    @Value("${rocketmq.hotelRoom.producer.group}")
    private String hotelRoomProducerGroup;

    /**
     * 酒店客房消息生产者
     *
     * @return 酒店客房消息生产者
     */
    @Bean(value = "hotelRoomMqProducer")
    public DefaultMQProducer hotelRoomMQProducer() throws MQClientException {
        DefaultMQProducer hotelRoomMQProducer=new DefaultMQProducer(hotelRoomProducerGroup);
        hotelRoomMQProducer.setNamesrvAddr(namesrvAddress);
        hotelRoomMQProducer.start();
        return hotelRoomMQProducer;
    }
}
