package com.ruyuan2020.little.project.rocketmq.api.order.controller;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan2020.little.project.rocketmq.api.order.dto.CreateOrderResponseDTO;
import com.ruyuan2020.little.project.rocketmq.api.order.dto.OrderInfoDTO;
import com.ruyuan2020.little.project.rocketmq.api.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    /**
     * 订单Service组件
     */
    @Autowired
    private OrderService orderService;

    /**
     * 预定房间 创建订单
     *
     * @param orderInfoDTO 订单信息
     * @return 结果   订单号
     */
    @PostMapping("/createOrder")
    public CommonResponse<CreateOrderResponseDTO> createOrder(@RequestBody OrderInfoDTO orderInfoDTO) {
        return orderService.createOrder(orderInfoDTO);
    }

    /**
     * 预定房间 未付款取消订单
     *
     * @param orderNo     订单编号
     * @param phoneNumber 手机号
     * @return 结果   订单号
     */
    @PostMapping(value = "cancelOrder")
    public CommonResponse cancelOrder(@RequestParam(value = "orderNo") String orderNo,
        @RequestParam(value = "phoneNumber") String phoneNumber) {
        return orderService.cancelOrder(orderNo, phoneNumber);
    }
}
