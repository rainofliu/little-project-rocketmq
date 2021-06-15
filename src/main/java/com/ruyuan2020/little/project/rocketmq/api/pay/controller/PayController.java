package com.ruyuan2020.little.project.rocketmq.api.pay.controller;

import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.redis.api.RedisApi;
import com.ruyuan2020.little.project.rocketmq.api.order.service.OrderService;
import com.ruyuan2020.little.project.rocketmq.api.pay.constants.PayTransactionStatusConstant;
import com.ruyuan2020.little.project.rocketmq.api.pay.dto.PayTransaction;
import com.ruyuan2020.little.project.rocketmq.api.pay.dto.QueryPayStatusResponse;
import com.ruyuan2020.little.project.rocketmq.api.pay.service.PayTransactionService;
import com.ruyuan2020.little.project.rocketmq.common.constants.PayTypeConstant;
import com.ruyuan2020.little.project.rocketmq.common.utils.DateUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.ruyuan2020.little.project.rocketmq.common.constants.RedisKeyConstant.ORDER_DUPLICATION_KEY_PREFIX;

@RestController
@RequestMapping(value = "/api/pay")
public class PayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private PayTransactionService payTransactionService;

    /**
     * 订单服务 ，在这里是本地调用，实际应该为RPC调用
     */
    @Autowired
    private OrderService orderService;

    /**
     * redis dubbo服务
     */
    @Reference(version = "1.0.0", interfaceClass = RedisApi.class, cluster = "failfast")
    private RedisApi redisApi;

    /**
     * 微信支付回调接口
     *
     * @param queryPayStatusResponse 支付回调响应
     * @return 结果 订单id
     */
    @PostMapping(value = "wx/callback")
    public CommonResponse<Integer> wxCallback(QueryPayStatusResponse queryPayStatusResponse) {
        String orderNo     = queryPayStatusResponse.getOrderNo();
        String phoneNumber = queryPayStatusResponse.getPhoneNumber();

        // 同一个订单多次支付保证接口幂等
        CommonResponse<Boolean> response = redisApi.setnx(ORDER_DUPLICATION_KEY_PREFIX + orderNo, orderNo, phoneNumber,
            LittleProjectTypeEnum.ROCKETMQ);

        if (Objects.equals(response.getCode(), ErrorCodeEnum.FAIL.getCode())) {
            LOGGER.info(" pay order redis dubbo interface error orderNo:{}", orderNo);
            return CommonResponse.fail();
        }

        // redis dubbo接口调用成功
        if (Objects.equals(response.getData(), Boolean.FALSE)) {
            // 重复订单 返回
            LOGGER.info("duplicate pay order orderNo:{}", orderNo);
            return CommonResponse.success();
        } else {

            PayTransaction payTransaction = new PayTransaction();
            payTransaction.setOrderNo(orderNo);
            payTransaction.setUserPayAccount(queryPayStatusResponse.getUserPayAccount());
            payTransaction.setTransactionNumber(queryPayStatusResponse.getTransactionNumber());
            payTransaction.setFinishPayTime(
                DateUtil.format(queryPayStatusResponse.getFinishPayTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
            payTransaction.setResponseCode(queryPayStatusResponse.getResponseCode());
            payTransaction.setTransactionChannel(PayTypeConstant.WX);
            payTransaction.setPayableAmount(queryPayStatusResponse.getPayableAmount());
            Integer status = queryPayStatusResponse.getPayTransactionStatus();
            payTransaction.setStatus(status);

            // 保存支付流水记录
            if (!payTransactionService.save(payTransaction, phoneNumber)) {
                // 失败 等待微信重试
                return CommonResponse.fail();
            }
            Integer orderId = null;
            if (Objects.equals(status, PayTransactionStatusConstant.SUCCESS)) {
                // 支付成功
                try {
                    orderId = orderService.informPayOrderSuccessed(orderNo, phoneNumber);
                } catch (Exception e) {
                    // 支付订单异常 删除 幂等的key
                    redisApi.del(ORDER_DUPLICATION_KEY_PREFIX + orderNo, phoneNumber, LittleProjectTypeEnum.ROCKETMQ);
                }
            }

            return CommonResponse.success(orderId);
        }

    }

}
