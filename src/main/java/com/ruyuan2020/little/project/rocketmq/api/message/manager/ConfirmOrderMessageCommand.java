package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan.little.project.message.api.WxSubscribeMessageApi;
import com.ruyuan.little.project.message.dto.ConfirmOrderMessageDTO;
import com.ruyuan.little.project.message.dto.ValueDTO;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;

/**
 * 入住订单
 */
public class ConfirmOrderMessageCommand extends AbstractOrderMessageCommand<ConfirmOrderMessageDTO> {

    public ConfirmOrderMessageCommand(WxSubscribeMessageApi wxSubscribeMessageApi) {
        super(wxSubscribeMessageApi);
    }

    /**
     * 构建消息内容 （模板方法，交给子类实现）
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    protected ConfirmOrderMessageDTO buildWxOrderMessage(OrderInfo orderInfo) {
        ConfirmOrderMessageDTO confirmOrderMessageDTO = new ConfirmOrderMessageDTO();
        ValueDTO                thing6                 = new ValueDTO();
        thing6.setValue(orderInfo.getOrderItem().getTitle());
        confirmOrderMessageDTO.setThing1(thing6);

        ValueDTO thing2 = new ValueDTO();
        thing2.setValue(MessageTypeEnum.WX_CONFIRM_ORDER.getDesc());
        confirmOrderMessageDTO.setThing2(thing2);

        return confirmOrderMessageDTO;
    }

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.WX_CONFIRM_ORDER;
    }
}
