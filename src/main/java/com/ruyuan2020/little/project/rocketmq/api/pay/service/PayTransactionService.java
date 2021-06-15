package com.ruyuan2020.little.project.rocketmq.api.pay.service;

import com.ruyuan2020.little.project.rocketmq.api.pay.dto.PayTransaction;

/**
 * 支付流水记录service接口组件
 * */
public interface PayTransactionService {

    /**
     * 保存支付流水记录
     *
     * @param payTransaction 支付流水
     * @param phoneNumber    手机号
     * @return 记录流水结果
     */
    Boolean save(PayTransaction payTransaction, String phoneNumber);
}
