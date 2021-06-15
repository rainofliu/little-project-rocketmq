package com.ruyuan2020.little.project.rocketmq.admin.service;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan2020.little.project.rocketmq.admin.dto.AdminHotelRoom;

/**
 * 后台系统房间管理service组件
 * */
public interface AdminRoomService {

    /**
     * 添加房间
     *
     * @param adminHotelRoom 房间内容
     * @return 结果
     */
    CommonResponse add(AdminHotelRoom adminHotelRoom);

    /**
     * 更新商品信息
     *
     * @param adminHotelRoom 请求体内容
     * @return
     */
    CommonResponse update(AdminHotelRoom adminHotelRoom);

}
