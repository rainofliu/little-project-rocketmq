package com.ruyuan2020.little.project.rocketmq.api.login.service;

import com.ruyuan2020.little.project.rocketmq.api.login.dto.LoginRequestDTO;

/**
 * 登录接口Service组件
 */
public interface LoginService {
    /**
     * 第一次登录 发 优惠券
     *
     * @param requestDTO 登录消息
     */
    void firstLoginDistributeCoupon(LoginRequestDTO requestDTO);
    /**
     * 重置用户登录状态
     * @param phoneNumber 手机号
     * */
    void resetFirstLoginStatus(String phoneNumber);
}
