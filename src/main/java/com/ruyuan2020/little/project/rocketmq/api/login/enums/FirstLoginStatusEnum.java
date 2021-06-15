package com.ruyuan2020.little.project.rocketmq.api.login.enums;

/**
 * 第一次登录状态 枚举
 *
 * @author ajin
 */
public enum FirstLoginStatusEnum {

    YES(1, "未登录过"),

    NO(2, "已登录过");

    private Integer status;

    private String desc;

    FirstLoginStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
