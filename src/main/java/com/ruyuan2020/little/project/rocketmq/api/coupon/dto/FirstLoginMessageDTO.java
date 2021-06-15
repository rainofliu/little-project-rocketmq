package com.ruyuan2020.little.project.rocketmq.api.coupon.dto;

/**
 * 第一次登录的消息 DTO
 *
 * @author ajin
 */
public class FirstLoginMessageDTO {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名称
     */
    private String  nickName;

    /**
     * 小程序id
     */
    private Integer beid;
    /**
     * 用户手机号
     */
    private String  phoneNumber;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getBeid() {
        return beid;
    }

    public void setBeid(Integer beid) {
        this.beid = beid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
