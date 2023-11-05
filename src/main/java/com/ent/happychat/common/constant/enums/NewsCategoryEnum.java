package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum NewsCategoryEnum {

    HEADLINES(1,"头条"),
    NEWS(2,"新闻"),
    SPORTS(3,"体育"),
    ENTERTAINMENT(4,"娱乐"),
    MILITARY_AFFAIRS(5,"军事"),
    SCIENCE(6,"科技"),
    PARENTING(7,"育儿"),
    WOMAN(8,"女性");

    @Getter
    @EnumValue
    private int code;

    @Getter
    @JsonValue
    private String name;

    NewsCategoryEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }
}
