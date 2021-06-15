package com.ruyuan2020.little.project.rocketmq.common.exception;
/**
 * 系统业务异常
 * @author ajin
 * */
public class BusinessException  extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
