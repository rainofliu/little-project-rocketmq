package com.ruyuan2020.little.project.rocketmq.admin.controller;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan2020.little.project.rocketmq.admin.dto.AdminHotelRoom;
import com.ruyuan2020.little.project.rocketmq.admin.service.AdminRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理系统-->房间管理组件
 *
 * @author ajin
 */
@RestController
@RequestMapping(value = "/admin/hotel/room")
public class AdminHotelRoomController {

    @Autowired
    private AdminRoomService adminRoomService;

    @PostMapping(value = "/add")
    public CommonResponse add(@RequestBody AdminHotelRoom adminHotelRoom) {
        return adminRoomService.add(adminHotelRoom);
    }

    @PostMapping(value = "/update")
    public CommonResponse update(@RequestBody AdminHotelRoom adminHotelRoom) {
        return adminRoomService.update(adminHotelRoom);
    }
}
