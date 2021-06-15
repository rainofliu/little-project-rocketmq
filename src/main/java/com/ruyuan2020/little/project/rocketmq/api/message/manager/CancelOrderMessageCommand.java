package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan.little.project.message.api.WxSubscribeMessageApi;
import com.ruyuan.little.project.message.dto.CancelOrderMessageDTO;
import com.ruyuan.little.project.message.dto.ValueDTO;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;
import com.ruyuan2020.little.project.rocketmq.common.utils.DateUtil;

import java.util.Date;

/**
 * 撤销订单
 */
public class CancelOrderMessageCommand extends AbstractOrderMessageCommand<CancelOrderMessageDTO> {

    public CancelOrderMessageCommand(WxSubscribeMessageApi wxSubscribeMessageApi) {
        super(wxSubscribeMessageApi);
    }

    /**
     * 构建消息内容 （模板方法，交给子类实现）
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    protected CancelOrderMessageDTO buildWxOrderMessage(OrderInfo orderInfo) {
        CancelOrderMessageDTO cancelOrderMessageDTO = new CancelOrderMessageDTO();
        ValueDTO                thing1                = new ValueDTO();
        thing1.setValue(orderInfo.getOrderItem().getTitle());
        cancelOrderMessageDTO.setThing1(thing1);

        ValueDTO time2      = new ValueDTO();
        long     createTime = orderInfo.getCancelTime() * 1000L;
        time2.setValue(DateUtil.format(new Date(createTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
        cancelOrderMessageDTO.setTime2(time2);

        ValueDTO character_string5 = new ValueDTO();
        character_string5.setValue(orderInfo.getOrderNo());
        cancelOrderMessageDTO.setCharacter_string5(character_string5);

        ValueDTO thing3 = new ValueDTO();
        thing3.setValue(MessageTypeEnum.WX_CANCEL_ORDER.getDesc());
        cancelOrderMessageDTO.setThing3(thing3);
        return cancelOrderMessageDTO;
    }

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.WX_CANCEL_ORDER;
    }
}
