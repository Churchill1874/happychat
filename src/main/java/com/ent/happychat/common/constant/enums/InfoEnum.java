package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 信息类型枚举
 */
public enum InfoEnum {

    NEWS(1,"新闻"),
    YOUTUBE(2,"youtube"),
    ABROAD(3,"境外"),
    WORK(4,"工作"),
    APPLICANT(5,"人才"),
    CHAT_GIRL(6,"聊妹");

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
