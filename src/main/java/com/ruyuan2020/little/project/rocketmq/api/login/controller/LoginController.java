package com.ruyuan2020.little.project.rocketmq.api.login.controller;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan2020.little.project.rocketmq.api.login.dto.LoginRequestDTO;
import com.ruyuan2020.little.project.rocketmq.api.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口
 *
 * @author ajin
 */
@RestController
@RequestMapping(value = "/api/login")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    /**
     * 登录Service实现
     */
    @Autowired
    private LoginService loginService;

    /**
     * 登录请求 http://ecs公网ip:8088/api/login/resetLoginStatus?phoneNumber=用户手机号
     *
     * @param loginRequestDTO 登录请求信息
     * @return 结果
     */
    @PostMapping(value = "/wxLogin")
    public CommonResponse wxLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        // TODO 模拟接收到用户 登录请求
        LOGGER.info("login success user info: {}", JSON.toJSONString(loginRequestDTO));

        // 第一次登录下发优惠券
        loginService.firstLoginDistributeCoupon(loginRequestDTO);

        return CommonResponse.success();
    }

    /**
     * 重置登录状态 TODO 方便测试使用
     *
     * @param phoneNumber 用户手机号
     * @return 结果
     */
    @GetMapping(value = "/resetLoginStatus")
    public CommonResponse resetFirstLoginStatus(@RequestParam(value = "phoneNumber") String phoneNumber) {
        LOGGER.info("reset user first login status phoneNumber : {}", phoneNumber);
        loginService.resetFirstLoginStatus(phoneNumber);
        return CommonResponse.success();
    }
}
