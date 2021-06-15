package com.ruyuan2020.little.project.rocketmq.common.constants;

/**
 * redis 操作key常量
 *
 * @author ajin
 */
public class RedisKeyConstant {

    /**
     * 第一次登陆重复消费 保证幂等的key前缀
     */
    public static final String FIRST_LOGIN_DUPLICATION_KEY_PREFIX = "little:project:firstLoginDuplication:";

    /**
     * 酒店房间key的前缀
     */
    public static final String HOTEL_ROOM_KEY_PREFIX = "little:project:hotelRoom:";

    /**
     * 订单支付和取消分布式锁key
     */
    public static final String ORDER_LOCK_KEY_PREFIX = "little:project:orderLock:";

    /**
     * 订单重复支付
     */
    public static final String ORDER_DUPLICATION_KEY_PREFIX = "little:project:payOrderDuplication:";

    /**
     * 完成订单后重复消费 保证幂等key前缀
     */
    public static final String ORDER_FINISHED_DUPLICATION_KEY_PREFIX = "little:project:orderFinishedDuplication:";
}
