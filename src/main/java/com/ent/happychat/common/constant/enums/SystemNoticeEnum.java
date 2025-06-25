package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum SystemNoticeEnum {

    FOLLOW(1, "关注"),
    LIKES(2, "点赞"),
    ADMIN(3,"管理员通知"),//用于管理员身份针对某个用户或者大家说的话
    APPLICATION_TIPS(4,"系统提示"),//由系统的规则产生的应用提示 比如被封账号了
    ACTIVITY(5,"活动"),
    NOTICE(6,"公告")//用于宣传说明广而告之的事情 比如通缉 使用规则说明等
    ;

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    SystemNoticeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }


}
