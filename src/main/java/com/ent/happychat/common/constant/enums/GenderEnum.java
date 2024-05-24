package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum GenderEnum {
    FEMALE(0,"女"),
    MALE(1,"男");
    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    GenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
