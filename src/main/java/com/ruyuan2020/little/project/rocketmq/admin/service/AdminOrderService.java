package com.ruyuan2020.little.project.rocketmq.admin.service;

import com.ruyuan.little.project.common.dto.CommonResponse;

/**
 * 后台管理系统 订单管理组件
 * */
public interface AdminOrderService {

    /**
     * 确认订单入住
     *
     * @param orderNo     订单号
     * @param phoneNumber 手机号
     * @return 结果
     */
    CommonResponse confirmOrder(String orderNo, String phoneNumber);

    /**
     * 退房
     *
     * @param orderNo     订单编号
     * @param phoneNumber 手机号
     * @return 结果
     */
    CommonResponse finishedOrder(String orderNo, String phoneNumber);
}
