package com.ruyuan2020.little.project.rocketmq.api.hotel.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.redis.api.RedisApi;
import com.ruyuan2020.little.project.rocketmq.api.hotel.dto.HotelRoom;
import com.ruyuan2020.little.project.rocketmq.api.hotel.dto.HotelRoomMessage;
import com.ruyuan2020.little.project.rocketmq.api.hotel.service.impl.HotelRoomCacheManager;
import com.ruyuan2020.little.project.rocketmq.common.constants.RedisKeyConstant;
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

/**
 * 房间更新成功的Listener
 */
@Component
public class HotelRoomUpdateMessageListener implements MessageListenerConcurrently {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelRoomUpdateMessageListener.class);

    @Reference(version = "1.0.0", interfaceClass = RedisApi.class, cluster = "failfast")
    private RedisApi redisApi;

    /**
     * 酒店房间本地缓存管理器
     */
    @Autowired
    private HotelRoomCacheManager hotelRoomCacheManager;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        // 处理房间更新成功的消息
        // 从redis中取更新更新房间缓存信息
        for (MessageExt msg : msgs) {
            // 获取消息 -> String
            String body = new String(msg.getBody(), StandardCharsets.UTF_8);
            try {

                // 反序列化  String -> HotelRoomMessage
                HotelRoomMessage hotelRoomMessage = JSON.parseObject(body, HotelRoomMessage.class);

                Long roomId = hotelRoomMessage.getRoomId();
                LOGGER.info("receive room update message,roomId:{}", roomId);

                LOGGER.info("start query hotel room from redis cache param:{}", roomId);
                CommonResponse<String> commonResponse = redisApi.get(RedisKeyConstant.HOTEL_ROOM_KEY_PREFIX + roomId,
                    hotelRoomMessage.getPhoneNumber(), LittleProjectTypeEnum.ROCKETMQ);
                LOGGER.info("end query hotel room from redis cache param:{}", roomId);

                if (Objects.equals(commonResponse.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
                    LOGGER.info("update local hotel room cache ,data:{}", commonResponse.getData());
                    hotelRoomCacheManager.updateLocalCache(
                        JSON.parseObject(commonResponse.getData(), HotelRoom.class));
                }
            } catch (Exception e) {
                // 成功获取消息，但消费失败
                LOGGER.error("receive hotel room update message :{},consume fail", body);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        // 成功消费
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
