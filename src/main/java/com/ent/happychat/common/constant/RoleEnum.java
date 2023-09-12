package com.ent.happychat.common.constant;

import lombok.Getter;

/**
 * 角色枚举
 */
public enum RoleEnum {

    BOT(0,"机器人"),
    PLAYER(1,"玩家"),
    ADMIN(2,"管理员"),
    SUPER_ADMIN(3,"超级管理员");

    @Getter
    private int code;

    @Getter
    private String name;

    RoleEnum(int code,String name){
        this.code = code;
        this.name = name;
    }


}
