package com.ent.happychat.common.constant.enums;

import lombok.Getter;

/**
 * 日志枚举
 */
public enum LogTypeEnum {

    ERROR(1, "异常日志"),
    OPERATION(2, "操作日志"),
    LOGIN(3, "登录日志"),
    REGISTER(4, "注册日志"),
    WARN(5,"警告日志");

    @Getter
    private int value;

    @Getter
    private String name;

    LogTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
