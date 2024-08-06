package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ViewsEnum {

    NEWS(1,"新闻"),
    WORK(2,"工作"),
    APPLICANT(3,"人才"),
    CHAT_GIRL(4,"女孩"),
    BET(5,"投注");


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
