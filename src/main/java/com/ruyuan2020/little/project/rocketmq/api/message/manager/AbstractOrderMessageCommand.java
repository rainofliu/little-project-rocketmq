package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan.little.project.message.api.WxSubscribeMessageApi;
import com.ruyuan.little.project.message.dto.WxSubscribeMessageDTO;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单消息抽象模板类
 *
 * @author ajin
 */
public abstract class AbstractOrderMessageCommand<T> implements OrderMessageCommand<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOrderMessageCommand.class);

    /**
     * 微信订阅消息api接口
     */
    private WxSubscribeMessageApi wxSubscribeMessageApi;

    public AbstractOrderMessageCommand(WxSubscribeMessageApi wxSubscribeMessageApi) {
        this.wxSubscribeMessageApi = wxSubscribeMessageApi;
    }

    /**
     * 执行
     *
     * @param orderInfo 订单信息
     */
    @Override
    public void send(OrderInfo orderInfo) {
        T wxOrderMessage = this.buildWxOrderMessage(orderInfo);
        this.doSend(wxOrderMessage, orderInfo.getPhoneNumber());
    }

    /**
     * 构建消息内容 （模板方法，交给子类实现）
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    protected abstract T buildWxOrderMessage(OrderInfo orderInfo);

    private void doSend(T wxOrderMessage, String phoneNumber) {
        WxSubscribeMessageDTO<T> subscribeMessageDTO = new WxSubscribeMessageDTO<>();
        subscribeMessageDTO.setContent(wxOrderMessage);
        subscribeMessageDTO.setMessageTypeEnum(this.getMessageType());
        subscribeMessageDTO.setPhoneNumber(phoneNumber);
        subscribeMessageDTO.setLittleProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
        LOGGER.info("start push message to weixin,param:{}", JSON.toJSONString(subscribeMessageDTO));
        CommonResponse commonResponse = wxSubscribeMessageApi.send(subscribeMessageDTO);
        LOGGER.info("end push message to weixin,param:{} ,response:{}", JSON.toJSONString(subscribeMessageDTO),
            JSON.toJSONString(commonResponse));
    }


    /**
     * 消息类型
     *
     * @return 消息类型
     */
    protected abstract MessageTypeEnum getMessageType();
}
