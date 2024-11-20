package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ViewsEnum {

    NEWS(1,"新闻"),
    WORK(2,"工作"),
    Company(3,"公司"),
    CHAT_GIRL(4,"聊妹"),
    BET(5,"投注"),
    SoutheastAsia(6,"东南亚新闻");


    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    ViewsEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }


}
