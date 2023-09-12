package com.ent.happychat.common.constant;

import lombok.Getter;

/**
 * 用户状态枚举
 */
public enum UserStatusEnum {

    NORMAL("正常",1),
    DISABLE("禁用",0);

    @Getter
    private String name;

    @Getter
    private int value;

    UserStatusEnum(String name,int value){
        this.name = name;
        this.value = value;
    }

}
