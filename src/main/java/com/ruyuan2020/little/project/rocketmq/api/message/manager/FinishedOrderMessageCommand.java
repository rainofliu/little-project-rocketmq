package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan.little.project.message.api.WxSubscribeMessageApi;
import com.ruyuan.little.project.message.dto.FinishedOrderMessageDTO;
import com.ruyuan.little.project.message.dto.ValueDTO;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;

/**
 * 退房订单
 */
public class FinishedOrderMessageCommand extends AbstractOrderMessageCommand<FinishedOrderMessageDTO> {
    public FinishedOrderMessageCommand(WxSubscribeMessageApi wxSubscribeMessageApi) {
        super(wxSubscribeMessageApi);
    }

    /**
     * 构建消息内容 （模板方法，交给子类实现）
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    protected FinishedOrderMessageDTO buildWxOrderMessage(OrderInfo orderInfo) {
        FinishedOrderMessageDTO finishedOrderMessageDTO = new FinishedOrderMessageDTO();
        ValueDTO                character_string1       = new ValueDTO();
        character_string1.setValue(orderInfo.getOrderNo());
        finishedOrderMessageDTO.setCharacter_string1(character_string1);

        ValueDTO thing5 = new ValueDTO();
        thing5.setValue(MessageTypeEnum.WX_FINISHED_ORDER.getDesc());
        finishedOrderMessageDTO.setThing5(thing5);

        return finishedOrderMessageDTO;
    }

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.WX_FINISHED_ORDER;
    }
}
