package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum LotteryTypeEnum {

    POLITICS(1,"政治彩种"),
    FOOTBALL(2,"足球彩种");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    LotteryTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }



}
