package com.ruyuan2020.little.project.rocketmq.admin.controller;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan2020.little.project.rocketmq.admin.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;
    /**
     * 入住订单
     *
     * @param orderNo     订单号
     * @param phoneNumber 手机号
     * @return 结果
     */
    @PostMapping("/confirmOrder")
    public CommonResponse  confirmOrder(@RequestParam(value = "orderNo")String orderNo,
        @RequestParam(value = "phoneNumber")String phoneNumber){
        return adminOrderService.confirmOrder(orderNo, phoneNumber);
    }

    /**
     * 退房订单
     *
     * @param orderNo     订单号
     * @param phoneNumber 手机号
     * @return 结果
     */
    @GetMapping(value = "/finishedOrder")
    public CommonResponse finishedOrder(@RequestParam(value = "orderNo") String orderNo,
        @RequestParam(value = "phoneNumber") String phoneNumber) {
        return adminOrderService.finishedOrder(orderNo, phoneNumber);
    }
}
