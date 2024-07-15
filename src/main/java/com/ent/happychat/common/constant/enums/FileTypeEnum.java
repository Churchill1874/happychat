package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {

    IMAGE(1, "image"),
    AUDIO(2,"audio"),
    VIDEO(3, "video");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    FileTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
