package com.ruyuan2020.little.project.rocketmq.api.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.mysql.api.MysqlApi;
import com.ruyuan.little.project.mysql.dto.MysqlRequestDTO;
import com.ruyuan2020.little.project.rocketmq.api.coupon.service.CouponService;
import com.ruyuan2020.little.project.rocketmq.api.login.enums.CouponUsedStatusEnum;
import com.ruyuan2020.little.project.rocketmq.common.utils.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优惠券服务Service组件  实现类
 *
 * @author ajin
 */
@Service
public class CouponServiceImpl implements CouponService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponServiceImpl.class);

    /**
     * mysql dubbo服务
     */
    @Reference(version = "1.0.0", interfaceClass = MysqlApi.class, cluster = "failfast")
    private MysqlApi mysqlApi;

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
    @Override
    public void distributeCoupon(Integer beid, Integer userId, Integer couponConfigId, Integer validDay,
        Integer sourceOrderId, String phoneNumber) {

        MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
        mysqlRequestDTO.setSql(
            "INSERT INTO t_coupon_user (" + " coupon_id," + " beid," + " uid," + " begin_date," + " end_date, "
                + " source_order_id " + ") " + "VALUES " + "(" + " ?," + " ?," + " ?," + " ?," + " ?, " + " ? " + ")");
        List<Object> params = new ArrayList<>();
        params.add(couponConfigId);
        params.add(beid);
        params.add(userId);
        Date date = new Date();
        // 开始时间
        params.add(DateUtil.getDateFormat(date, DateUtil.Y_M_D_PATTERN));
        // 结束时间
        params.add(DateUtil.getDateFormat(DateUtils.addDays(date, validDay), DateUtil.Y_M_D_PATTERN));
        params.add(sourceOrderId);

        mysqlRequestDTO.setParams(params);
        mysqlRequestDTO.setPhoneNumber(phoneNumber);
        mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
        // 保存用户优惠券
        LOGGER.info("start save user coupon param:{}", JSON.toJSONString(mysqlRequestDTO));
        CommonResponse<Integer> response = mysqlApi.insert(mysqlRequestDTO);
        LOGGER.info("end save user coupon param:{}, response:{}", JSON.toJSONString(mysqlRequestDTO),
            JSON.toJSONString(response));

    }

    /**
     * 使用优惠券
     *
     * @param orderId     订单id
     * @param couponId    优惠券id
     * @param phoneNumber 用户手机号
     */
    @Override
    public void usedCoupon(Integer orderId, Integer couponId, String phoneNumber) {
        MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
        mysqlRequestDTO.setSql("UPDATE t_coupon_user "
            + "SET "
            + "used_time = ?,"
            + "used_order_id = ?,"
            + "used = ? "
            + "WHERE "
            + "id = ?");
        List<Object> params = new ArrayList<>();
        params.add(new Date());
        params.add(orderId);
        params.add(CouponUsedStatusEnum.ALREADY_USED.getStatus());
        params.add(couponId);
        mysqlRequestDTO.setParams(params);
        mysqlRequestDTO.setPhoneNumber(phoneNumber);
        mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);

        // 修改优惠券状态
        LOGGER.info("start used coupon param:{}", JSON.toJSONString(params));
        CommonResponse<Integer> response = mysqlApi.update(mysqlRequestDTO);
        LOGGER.info("end used coupon param:{}, response:{}", JSON.toJSONString(params), JSON.toJSONString(response));
    }

    @Override
    public void backUsedCoupon(Integer couponId, String phoneNumber) {
        MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
        mysqlRequestDTO.setSql("UPDATE t_coupon_user "
            + "SET "
            + "used_time = '0000-00-00 00:00:00',"
            + "used_order_id = 0,"
            + "used = ? "
            + "WHERE "
            + "id = ?");
        List<Object> params = new ArrayList<>();
        params.add(CouponUsedStatusEnum.NOT_USED.getStatus());
        params.add(couponId);
        mysqlRequestDTO.setParams(params);
        mysqlRequestDTO.setPhoneNumber(phoneNumber);
        mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);

        // 修改优惠券状态
        LOGGER.info("start back used coupon param:{}", JSON.toJSONString(params));
        CommonResponse<Integer> response = mysqlApi.update(mysqlRequestDTO);
        LOGGER.info("end back used coupon param:{}, response:{}", JSON.toJSONString(params), JSON.toJSONString(response));
    }
}
