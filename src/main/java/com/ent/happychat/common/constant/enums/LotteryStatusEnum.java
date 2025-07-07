package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum LotteryStatusEnum {

    PENDING(0,"待开奖"),
    SETTLED(1,"以开奖"),
    CANCELLED(2,"已取消");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    LotteryStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }


}
