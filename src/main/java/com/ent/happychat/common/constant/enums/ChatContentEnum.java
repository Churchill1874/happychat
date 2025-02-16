package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ChatContentEnum {

    CHARACTERS(1,"文字"),
    IMAGE(2,"图片"),
    VOICE(3, "声音");
    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    ChatContentEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }


}
