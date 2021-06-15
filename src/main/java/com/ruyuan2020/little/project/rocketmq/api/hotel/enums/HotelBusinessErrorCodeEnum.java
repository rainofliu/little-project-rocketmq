package com.ruyuan2020.little.project.rocketmq.api.hotel.enums;

/**
 * 酒店系统业务code码
 */
public enum HotelBusinessErrorCodeEnum {
    /**
     * 酒店房间不存在
     */
    HOTEL_ROOM_NOT_EXIST(580, "酒店房间不存在"),

    ;

    private int code;

    private String msg;

    HotelBusinessErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
