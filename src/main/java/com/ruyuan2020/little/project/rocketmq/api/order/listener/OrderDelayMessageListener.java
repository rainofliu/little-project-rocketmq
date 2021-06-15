package com.ruyuan2020.little.project.rocketmq.api.order.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.redis.api.RedisApi;
import com.ruyuan2020.little.project.rocketmq.api.order.dto.OrderInfoDTO;
import com.ruyuan2020.little.project.rocketmq.api.order.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.ruyuan2020.little.project.rocketmq.common.constants.RedisKeyConstant.ORDER_LOCK_KEY_PREFIX;

@Component(value = "orderDelayMessageListener")
public class OrderDelayMessageListener implements MessageListenerConcurrently {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDelayMessageListener.class);

    @Autowired
    private OrderService orderService;

    /**
     * redis dubbo服务
     */
    @Reference(version = "1.0.0", interfaceClass = RedisApi.class, cluster = "failfast")
    private RedisApi redisApi;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            String       content      = new String(msg.getBody(), StandardCharsets.UTF_8);
            OrderInfoDTO orderInfoDTO = JSON.parseObject(content, OrderInfoDTO.class);

            String phoneNumber = orderInfoDTO.getPhoneNumber();
            String orderNo     = orderInfoDTO.getOrderNo();
            LOGGER.info("receive order delay message,orderNo:{}", orderNo);

            // 获取订单分布式锁防止订单正在支付
            try {
                CommonResponse<Boolean> commonResponse = redisApi.lock(ORDER_LOCK_KEY_PREFIX + orderNo, orderNo, 10L,
                    TimeUnit.SECONDS, phoneNumber, LittleProjectTypeEnum.ROCKETMQ);
                if (Objects.equals(commonResponse.getCode(), ErrorCodeEnum.SUCCESS.getCode()) && Objects.equals(
                    commonResponse.getData(), Boolean.TRUE)) {
                    LOGGER.info("acquire distribute lock success");
                    try {
                        orderService.cancelOrder(orderNo, phoneNumber);
                    } catch (Exception e) {
                        LOGGER.error("cancel order fail error message:{}", e.getMessage());
                    }
                }
            } finally {
                // 释放锁
                redisApi.unlock(ORDER_LOCK_KEY_PREFIX + orderNo, orderNo, phoneNumber, LittleProjectTypeEnum.ROCKETMQ);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
