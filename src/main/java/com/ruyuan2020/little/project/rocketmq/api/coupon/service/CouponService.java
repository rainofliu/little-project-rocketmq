package com.ruyuan2020.little.project.rocketmq.api.coupon.service;

/**
 * 优惠券服务Service组件
 * @author ajin
 */
public interface CouponService {

    /**
     * 分发第一次登录的优惠券
     *
     * @param beid           小程序id
     * @param userId         用户id
     * @param couponConfigId 下发优惠券配置id
     * @param validDay       有效天数
     * @param sourceOrderId  优惠券来源订单id
     * @param phoneNumber    手机号
     */
    void distributeCoupon(Integer beid, Integer userId, Integer couponConfigId, Integer validDay, Integer sourceOrderId,
        String phoneNumber);

    /**
     * 使用优惠券
     *
     * @param orderId     订单id
     * @param couponId    优惠券id
     * @param phoneNumber 用户手机号
     */
    void usedCoupon(Integer orderId, Integer couponId, String phoneNumber);


    /**
     * 退回已使用的优惠券
     *
     * @param couponId    优惠券id
     * @param phoneNumber 手机号
     */
    void backUsedCoupon(Integer couponId, String phoneNumber);
}
