package com.ruyuan2020.little.project.rocketmq.api.message.manager;

import com.ruyuan.little.project.common.enums.MessageTypeEnum;
import com.ruyuan.little.project.message.api.WxSubscribeMessageApi;
import com.ruyuan.little.project.message.dto.ValueDTO;
import com.ruyuan.little.project.message.dto.WaitPayOrderMessageDTO;
import com.ruyuan2020.little.project.rocketmq.api.message.dto.OrderInfo;
import com.ruyuan2020.little.project.rocketmq.common.utils.DateUtil;

import java.util.Date;

/**
 * 待支付消息Command
 */
public class WaitPayOrderMessageCommand extends AbstractOrderMessageCommand<WaitPayOrderMessageDTO> {
    public WaitPayOrderMessageCommand(WxSubscribeMessageApi wxSubscribeMessageApi) {
        super(wxSubscribeMessageApi);
    }

    /**
     * 构建消息内容 （模板方法，交给子类实现）
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    protected WaitPayOrderMessageDTO buildWxOrderMessage(OrderInfo orderInfo) {
        WaitPayOrderMessageDTO waitPayOrderMessageDTO = new WaitPayOrderMessageDTO();
        ValueDTO               number1                = new ValueDTO();
        // TODO 由于模板字段只能为数字这里用订单id
        number1.setValue(orderInfo.getId());
        waitPayOrderMessageDTO.setNumber1(number1);

        ValueDTO time2      = new ValueDTO();
        long     createTime = orderInfo.getCreateTime() * 1000L;
        time2.setValue(DateUtil.format(new Date(createTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
        waitPayOrderMessageDTO.setTime2(time2);

        ValueDTO time10 = new ValueDTO();
        // 创建时间30分钟之后
        long validTime = (orderInfo.getCreateTime() + 30 * 60) * 1000L;
        time10.setValue(DateUtil.format(new Date(validTime), DateUtil.FULL_TIME_SPLIT_PATTERN));
        waitPayOrderMessageDTO.setTime10(time10);

        ValueDTO thing3 = new ValueDTO();
        thing3.setValue(orderInfo.getOrderItem().getTitle());
        waitPayOrderMessageDTO.setThing3(thing3);
        return waitPayOrderMessageDTO;
    }

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.WX_CREATE_ORDER;
    }
}
