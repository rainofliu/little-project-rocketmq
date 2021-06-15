package com.ruyuan2020.little.project.rocketmq.api.pay.constants;

/**
 * 支付交易状态相关常量
 */
public class PayTransactionStatusConstant {

    /**
     * 未付款
     */
    public static final Integer UN_PAYED = 1;

    /**
     * 支付成功
     */
    public static final Integer SUCCESS = 2;

    /**
     * 支付失败
     */
    public static final Integer FAILURE = 3;

    /**
     * 支付交易关闭
     */
    public static final Integer CLOSED = 4;

    private PayTransactionStatusConstant() {

    }
}
