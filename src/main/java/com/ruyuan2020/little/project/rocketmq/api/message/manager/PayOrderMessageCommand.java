package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan.little.project.message.api.WxSubscribeMessageApi;
import com.ruyuan.little.project.message.dto.PayOrderMessageDTO;
import com.ruyuan.little.project.message.dto.ValueDTO;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;
import com.ruyuan2020.little.project.rocketmq.common.utils.DateUtil;

import java.util.Date;

/**
 * 支付订单
 * */
public class PayOrderMessageCommand extends AbstractOrderMessageCommand<PayOrderMessageDTO> {
    public PayOrderMessageCommand(WxSubscribeMessageApi wxSubscribeMessageApi) {
        super(wxSubscribeMessageApi);
    }

    /**
     * 构建消息内容 （模板方法，交给子类实现）
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    protected PayOrderMessageDTO buildWxOrderMessage(OrderInfo orderInfo) {
        PayOrderMessageDTO payOrderMessageDTO = new PayOrderMessageDTO();
        ValueDTO            thing6             = new ValueDTO();
        thing6.setValue(orderInfo.getOrderItem().getTitle());
        payOrderMessageDTO.setThing6(thing6);

        ValueDTO character_string9 = new ValueDTO();
        character_string9.setValue(orderInfo.getOrderNo());
        payOrderMessageDTO.setCharacter_string9(character_string9);

        ValueDTO date10  = new ValueDTO();
        long     payTime = orderInfo.getPayTime() * 1000L;
        date10.setValue(DateUtil.format(new Date(payTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
        payOrderMessageDTO.setDate10(date10);

        ValueDTO thing5 = new ValueDTO();
        thing5.setValue(MessageTypeEnum.WX_PAY_ORDER.getDesc());
        payOrderMessageDTO.setThing5(thing5);

        return payOrderMessageDTO;
    }

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.WX_PAY_ORDER;
    }
}
