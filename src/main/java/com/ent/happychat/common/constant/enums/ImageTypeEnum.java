package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ImageTypeEnum {

    BANNER(1, "首页轮播图"),
    COMMENTS(2, "评论页面广告"),
    HOME(3,"首页广告"),
    MESSAGE(4,"消息页面广告");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    ImageTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
