package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DrawStatusEnum {

    WAITING(0,"待开奖"),
    WINNER(1,"中奖"),
    LOSS(2,"未中奖"),
    CANCEL(3,"取消事件");
    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    DrawStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }



}
