package com.ruyuan2020.little.project.rocketmq.admin.service.impl;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan2020.little.project.rocketmq.admin.service.AdminOrderService;
import com.ruyuan2020.little.project.rocketmq.api.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    private OrderService orderService;

    @Override
    public CommonResponse confirmOrder(String orderNo, String phoneNumber) {
        // TODO 正常调用订单服务的dubbo接口或者操作数据库
        orderService.informConfirmOrder(orderNo, phoneNumber);
        return CommonResponse.success();
    }

    /**
     * 退房
     *
     * @param orderNo     订单编号
     * @param phoneNumber 手机号
     * @return 结果
     */
    @Override
    public CommonResponse finishedOrder(String orderNo, String phoneNumber) {
        // TODO 正常调用订单服务的dubbo接口或者操作数据库
        orderService.informFinishedOrder(orderNo, phoneNumber);
        return CommonResponse.success();
    }

}
