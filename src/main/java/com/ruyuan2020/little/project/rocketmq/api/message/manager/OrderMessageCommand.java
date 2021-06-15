package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;

/**
 * 订单消息Command
 *
 * @author ajin
 */
public interface OrderMessageCommand<T> {
    /**
     * 执行
     *
     * @param orderInfo 订单信息
     */
    void send(OrderInfo orderInfo);
}
