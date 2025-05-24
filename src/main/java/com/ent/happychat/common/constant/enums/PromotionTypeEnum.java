package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PromotionTypeEnum {


    GOODS(1, "商品"),
    LIFE(2,"生活"),
    BUSINESS(3,"商业")
    ;


    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    PromotionTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }



}
