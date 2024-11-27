package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum NewsSourceEnum {


    CHINA(1, "国内新闻"),
    SOUTHEAST_ASIA(2,"东南亚新闻");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    NewsSourceEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
