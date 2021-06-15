package com.ruyuan2020.little.project.rocketmq.api.message.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderMessage;
import com.ruyuan2020.little.project.rocketmq.api.message.manager.OrderMessageCommand;
import com.ruyuan2020.little.project.rocketmq.api.message.manager.OrderMessageCommandFactory;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 订单状态<b>顺序消息</b>
 */
@Component(value = "orderMessageListener")
public class OrderMessageListener implements MessageListenerOrderly {
    /**
     * 日志组件
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageListener.class);

    // /**
    //  * mysql dubbo api接口
    //  */
    // @Reference(version = "1.0.0", interfaceClass = WxSubscribeMessageApi.class, cluster = "failfast")
    // private WxSubscribeMessageApi wxSubscribeMessageApi;
    @Autowired
    private OrderMessageCommandFactory orderMessageCommandFactory;

    /**
     * It is not recommend to throw exception,rather than returning ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT
     * if consumption failure
     *
     * @param msgs    msgs.size() >= 1<br> DefaultMQPushConsumer.consumeMessageBatchMaxSize=1,you can modify here
     * @param context
     * @return The consume status
     */
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

        for (MessageExt msg : msgs) {
            // 消息内容 String字符串
            String content = new String(msg.getBody(), StandardCharsets.UTF_8);
            LOGGER.info("receive order message : {}", content);

            // 订单消息对象
            OrderMessage orderMessage = JSON.parseObject(content, OrderMessage.class);
            // 订单内容对象
            OrderInfo orderInfo = JSON.parseObject(orderMessage.getContent(), OrderInfo.class);
            // 消息类型
            MessageTypeEnum     messageType         = orderMessage.getMessageType();

            OrderMessageCommand orderMessageCommand = orderMessageCommandFactory.create(messageType);

            // 发送消息
            try {

                orderMessageCommand.send(orderInfo);
                // if (Objects.equals(messageType, MessageTypeEnum.WX_CREATE_ORDER)) {
                //     WaitPayOrderMessageDTO waitPayOrderMessageDTO = this.builderWaitPayOrderMessage(orderInfo);
                //     this.send(waitPayOrderMessageDTO, orderInfo.getPhoneNumber(), messageType);
                // } else if (Objects.equals(messageType, MessageTypeEnum.WX_CANCEL_ORDER)) {
                //     CancelOrderMessageDTO cancelOrderMessageDTO = this.builderCancelOrderMessage(orderInfo);
                //     this.send(cancelOrderMessageDTO, orderInfo.getPhoneNumber(), messageType);
                // } else if (Objects.equals(messageType, MessageTypeEnum.WX_PAY_ORDER)) {
                //     PayOrderMessageDTO payOrderMessageDTO = this.builderPayOrderMessage(orderInfo);
                //     this.send(payOrderMessageDTO, orderInfo.getPhoneNumber(), messageType);
                // } else if (Objects.equals(messageType, MessageTypeEnum.WX_CONFIRM_ORDER)) {
                //     ConfirmOrderMessageDTO confirmOrderMessageDTO = this.builderConfirmOrderMessage(orderInfo);
                //     this.send(confirmOrderMessageDTO, orderInfo.getPhoneNumber(), messageType);
                // } else if (Objects.equals(messageType, MessageTypeEnum.WX_FINISHED_ORDER)) {
                //     FinishedOrderMessageDTO confirmOrderMessageDTO = this.builderFinishedOrderMessage(orderInfo);
                //     this.send(confirmOrderMessageDTO, orderInfo.getPhoneNumber(), messageType);
                // }
            } catch (Exception exception) {
                LOGGER.error("push wx message fail,error message:{}", exception.getMessage());
                // 发送消息给微信 失败 Suspend current queue a moment
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }

        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    // /**
    //  * 构建取消订单的消息内容
    //  *
    //  * @param orderInfo 订单信息
    //  * @return 结果
    //  */
    // private CancelOrderMessageDTO builderCancelOrderMessage(OrderInfo orderInfo) {
    //     CancelOrderMessageDTO cancelOrderMessageDTO = new CancelOrderMessageDTO();
    //     ValueDTO              thing1                = new ValueDTO();
    //     thing1.setValue(orderInfo.getOrderItem().getTitle());
    //     cancelOrderMessageDTO.setThing1(thing1);
    //
    //     ValueDTO time2      = new ValueDTO();
    //     long     createTime = orderInfo.getCancelTime() * 1000L;
    //     time2.setValue(DateUtil.format(new Date(createTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
    //     cancelOrderMessageDTO.setTime2(time2);
    //
    //     ValueDTO character_string5 = new ValueDTO();
    //     character_string5.setValue(orderInfo.getOrderNo());
    //     cancelOrderMessageDTO.setCharacter_string5(character_string5);
    //
    //     ValueDTO thing3 = new ValueDTO();
    //     thing3.setValue(MessageTypeEnum.WX_CANCEL_ORDER.getDesc());
    //     cancelOrderMessageDTO.setThing3(thing3);
    //     return cancelOrderMessageDTO;
    // }
    //
    // /**
    //  * 构建微信站内信消息
    //  *
    //  * @param orderInfo 订单信息
    //  * @return 结果
    //  */
    // private PayOrderMessageDTO builderPayOrderMessage(OrderInfo orderInfo) {
    //     PayOrderMessageDTO payOrderMessageDTO = new PayOrderMessageDTO();
    //     ValueDTO           thing6             = new ValueDTO();
    //     thing6.setValue(orderInfo.getOrderItem().getTitle());
    //     payOrderMessageDTO.setThing6(thing6);
    //
    //     ValueDTO character_string9 = new ValueDTO();
    //     character_string9.setValue(orderInfo.getOrderNo());
    //     payOrderMessageDTO.setCharacter_string9(character_string9);
    //
    //     ValueDTO date10  = new ValueDTO();
    //     long     payTime = orderInfo.getPayTime() * 1000L;
    //     date10.setValue(DateUtil.format(new Date(payTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
    //     payOrderMessageDTO.setDate10(date10);
    //
    //     ValueDTO thing5 = new ValueDTO();
    //     thing5.setValue(MessageTypeEnum.WX_PAY_ORDER.getDesc());
    //     payOrderMessageDTO.setThing5(thing5);
    //
    //     return payOrderMessageDTO;
    // }
    //
    // /**
    //  * 创建待支付订单消息
    //  *
    //  * @param orderInfo 订单信息
    //  * @return 待支付消息
    //  */
    // private WaitPayOrderMessageDTO builderWaitPayOrderMessage(OrderInfo orderInfo) {
    //     // 待支付订单信息
    //     WaitPayOrderMessageDTO waitPayOrderMessageDTO = new WaitPayOrderMessageDTO();
    //
    //     ValueDTO number1 = new ValueDTO();
    //     // TODO 由于模板字段只能为数字这里用订单id
    //     number1.setValue(orderInfo.getId());
    //     waitPayOrderMessageDTO.setNumber1(number1);
    //
    //     ValueDTO time2      = new ValueDTO();
    //     long     createTime = orderInfo.getCreateTime() * 1000L;
    //     time2.setValue(DateUtil.format(new Date(createTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
    //     waitPayOrderMessageDTO.setTime2(time2);
    //
    //     ValueDTO time10 = new ValueDTO();
    //     // 创建时间30分钟之后
    //     long validTime = (orderInfo.getCreateTime() + 30 * 60) * 1000L;
    //     time10.setValue(DateUtil.format(new Date(validTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
    //     waitPayOrderMessageDTO.setTime10(time10);
    //
    //     ValueDTO thing3 = new ValueDTO();
    //     thing3.setValue(orderInfo.getOrderItem().getTitle());
    //     waitPayOrderMessageDTO.setThing3(thing3);
    //
    //     return waitPayOrderMessageDTO;
    // }
    //
    // /**
    //  * 构建入住订单站内信消息
    //  *
    //  * @param orderInfo 订单信息
    //  * @return 结果
    //  */
    // protected ConfirmOrderMessageDTO builderConfirmOrderMessage(OrderInfo orderInfo) {
    //     ConfirmOrderMessageDTO confirmOrderMessageDTO = new ConfirmOrderMessageDTO();
    //     ValueDTO               thing6                 = new ValueDTO();
    //     thing6.setValue(orderInfo.getOrderItem().getTitle());
    //     confirmOrderMessageDTO.setThing1(thing6);
    //
    //     ValueDTO thing2 = new ValueDTO();
    //     thing2.setValue(MessageTypeEnum.WX_CONFIRM_ORDER.getDesc());
    //     confirmOrderMessageDTO.setThing2(thing2);
    //
    //     return confirmOrderMessageDTO;
    // }
    //
    // /**
    //  * 构建入住订单 退房 站内信消息
    //  *
    //  * @param orderInfo 订单信息
    //  * @return 结果
    //  */
    // private FinishedOrderMessageDTO builderFinishedOrderMessage(OrderInfo orderInfo) {
    //     FinishedOrderMessageDTO finishedOrderMessageDTO = new FinishedOrderMessageDTO();
    //     ValueDTO character_string1 = new ValueDTO();
    //     character_string1.setValue(orderInfo.getOrderNo());
    //     finishedOrderMessageDTO.setCharacter_string1(character_string1);
    //
    //     ValueDTO thing5 = new ValueDTO();
    //     thing5.setValue(MessageTypeEnum.WX_FINISHED_ORDER.getDesc());
    //     finishedOrderMessageDTO.setThing5(thing5);
    //
    //     return finishedOrderMessageDTO;
    // }
    //
    // /**
    //  * 实际发送消息
    //  *
    //  * @param wxOrderMessage 消息内容
    //  * @param phoneNumber    手机号
    //  * @param messageType    消息类型
    //  */
    //
    // private <T> void send(T wxOrderMessage, String phoneNumber, MessageTypeEnum messageType) {
    //     WxSubscribeMessageDTO<T> subscribeMessageDTO = new WxSubscribeMessageDTO<>();
    //     subscribeMessageDTO.setContent(wxOrderMessage);
    //     subscribeMessageDTO.setMessageTypeEnum(messageType);
    //     subscribeMessageDTO.setPhoneNumber(phoneNumber);
    //     subscribeMessageDTO.setLittleProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
    //     LOGGER.info("start push message to weixin,param:{}", JSON.toJSONString(subscribeMessageDTO));
    //     CommonResponse commonResponse = wxSubscribeMessageApi.send(subscribeMessageDTO);
    //     LOGGER.info("end push message to weixin,param:{} ,response:{}", JSON.toJSONString(subscribeMessageDTO),
    //         JSON.toJSONString(commonResponse));
    // }
}
