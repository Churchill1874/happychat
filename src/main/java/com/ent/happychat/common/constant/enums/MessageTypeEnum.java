package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MessageTypeEnum {

    SYSTEM(1, "系统"),
    COMMENT(2,"评论");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    MessageTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }
}
