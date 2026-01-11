package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum CampEnum {

    NO(0, "无"),
    RED(1,"红营"),
    BLUE(2,"红营")
    ;
    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    CampEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }




}
