package com.ruyuan2020.little.project.rocketmq.api.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruyuan2020.little.project.rocketmq.api.hotel.dto.HotelRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 酒店房间缓存管理组件 用户维护酒店数据jvm内存
 *
 * @author ajin
 */
@Component
public class HotelRoomCacheManager {

    private static final Logger                             LOGGER            = LoggerFactory.getLogger(
        HotelRoomCacheManager.class);
    /**
     * 酒店房间jvm缓存 TODO 防止oom可以通过google Guava Cache改造
     */
    private              ConcurrentHashMap<Long, HotelRoom> hotelRoomCacheMap = new ConcurrentHashMap<>();

    /**
     * 根据房间id获取房间jvm内存信息
     *
     * @param roomId 房间id
     * @return 结果
     */
    public HotelRoom getHotelRoomFromLocalCache(Long roomId) {
        return hotelRoomCacheMap.get(roomId);
    }
    /**
     * 更新本地缓存
     *
     * @param hotelRoom 酒店房间数据
     */
    public void updateLocalCache(HotelRoom hotelRoom) {
        hotelRoomCacheMap.put(hotelRoom.getId(), hotelRoom);
        LOGGER.info("hotel room local cache data: {}", JSON.toJSONString(hotelRoom));
    }
}
