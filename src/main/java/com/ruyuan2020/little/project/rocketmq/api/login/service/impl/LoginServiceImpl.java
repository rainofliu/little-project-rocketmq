package com.ruyuan2020.little.project.rocketmq.api.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.mysql.api.MysqlApi;
import com.ruyuan.little.project.mysql.dto.MysqlRequestDTO;
import com.ruyuan.little.project.redis.api.RedisApi;
import com.ruyuan2020.little.project.rocketmq.api.login.dto.LoginRequestDTO;
import com.ruyuan2020.little.project.rocketmq.api.login.enums.FirstLoginStatusEnum;
import com.ruyuan2020.little.project.rocketmq.api.login.service.LoginService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 登录组件Service接口实现 -> Spring Bean
 */
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * 日志组件
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    /**
     * 日志生产者组件
     */
    @Autowired
    @Qualifier(value = "loginMQProducer")
    private DefaultMQProducer loginMQProducer;

    /**
     * MQ 登录通知Topic
     */
    @Value("${rocketmq.login.topic}")
    private String loginTopic;

    /**
     * mysql dubbo api 接口
     */
    @Reference(version = "1.0.0", interfaceClass = MysqlApi.class, cluster = "failfast")
    private MysqlApi mysqlApi;

    /**
     * redis dubbo 服务
     **/
    @Reference(version = "1.0.0", interfaceClass = RedisApi.class, cluster = "failfast")
    private RedisApi redisApi;

    @Override
    public void firstLoginDistributeCoupon(LoginRequestDTO loginRequestDTO) {
        if (!isFirstLogin(loginRequestDTO)) {
            // 不是第一次登录，直接返回
            LOGGER.info("userId:{} is not first login ", loginRequestDTO.getUserId());
            return;
        }

        // 更新第一次登录的标识位
        this.updateFirstLoginStatus(loginRequestDTO.getPhoneNumber(), FirstLoginStatusEnum.NO);

        // 发送第一次登录成功的消息
        this.sendFirstLoginMessage(loginRequestDTO);
    }

    @Override
    public void resetFirstLoginStatus(String phoneNumber) {
        this.updateFirstLoginStatus(phoneNumber, FirstLoginStatusEnum.YES);
    }

    /**
     * 校验是否为第一次登录
     *
     * @param loginRequestDTO 登录请求信息
     * @return
     */
    private boolean isFirstLogin(LoginRequestDTO loginRequestDTO) {
        MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();

        mysqlRequestDTO.setSql("select first_login_status from t_member where id = ? ");

        List<Object> params = new ArrayList<>();
        params.add(loginRequestDTO.getUserId());
        mysqlRequestDTO.setParams(params);
        mysqlRequestDTO.setPhoneNumber(loginRequestDTO.getPhoneNumber());
        mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);

        // 打印请求参数
        LOGGER.info("start query first login status param: {}", JSON.toJSONString(mysqlRequestDTO));
        // 请求mysql dubbo服务提供者，获取响应
        CommonResponse<List<Map<String, Object>>> response = mysqlApi.query(mysqlRequestDTO);
        // 打印请求响应
        LOGGER.info("end query first login status param: {},response :{}", JSON.toJSONString(mysqlRequestDTO),
            JSON.toJSONString(response));

        // 处理响应结果
        if (Objects.equals(response.getCode(), ErrorCodeEnum.SUCCESS.getCode()) && !CollectionUtils.isEmpty(
            response.getData())) {
            Map<String, Object> map = response.getData().get(0);
            // 判断是否为第一次登录
            return Objects.equals(Integer.valueOf(String.valueOf(map.get("first_login_status"))),
                FirstLoginStatusEnum.YES.getStatus());
        }
        return false;

    }

    /**
     * 更新第一次登录的标识位
     *
     * @param phoneNumber          手机号
     * @param firstLoginStatusEnum 登录状态 {@link FirstLoginStatusEnum}
     */
    private void updateFirstLoginStatus(String phoneNumber, FirstLoginStatusEnum firstLoginStatusEnum) {
        MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
        // 1563 为 酒店系统 小程序的id
        mysqlRequestDTO.setSql("update t_member set first_login_status = ? WHERE beid = 1563 and mobile = ?");
        List<Object> params = new ArrayList<>();
        params.add(firstLoginStatusEnum.getStatus());
        params.add(phoneNumber);
        mysqlRequestDTO.setParams(params);
        mysqlRequestDTO.setPhoneNumber(phoneNumber);
        mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);

        LOGGER.info("start update first login status, param : {}", JSON.toJSONString(mysqlRequestDTO));
        CommonResponse<Integer> response = mysqlApi.update(mysqlRequestDTO);
        LOGGER.info("end update first login status , param:{} ,response:{}", JSON.toJSONString(mysqlRequestDTO),
            JSON.toJSONString(response));
    }

    /**
     * 发送首次登录消息到rocketmq中
     * @param loginRequestDTO 登录请求信息
     */
    private void sendFirstLoginMessage(LoginRequestDTO loginRequestDTO) {
        // 场景一:性能提升  异步发送一个登录成功的消息到mq中
        Message message = new Message();
        message.setTopic(loginTopic);
        message.setBody(JSON.toJSONString(loginRequestDTO).getBytes(StandardCharsets.UTF_8));

        try {
            LOGGER.info(" start send login success notify message");
            SendResult sendResult = loginMQProducer.send(message);
            LOGGER.info(" end send login success notify message, send result : {}", JSON.toJSONString(sendResult));
        } catch (Exception e) {
            LOGGER.error("send login success notify message fail, error message : {}",e.getMessage());
        }
    }
}
