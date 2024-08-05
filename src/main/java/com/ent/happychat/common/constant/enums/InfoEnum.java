package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 信息类型枚举
 */
public enum InfoEnum {

    NEWS(1,"新闻"),
    WORK(2,"工作"),
    APPLICANT(3,"人才"),
    CHAT_GIRL(4,"聊妹");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    InfoEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
