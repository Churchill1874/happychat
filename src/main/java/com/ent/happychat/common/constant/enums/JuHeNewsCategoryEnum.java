package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

//聚合新闻类型
public enum JuHeNewsCategoryEnum {

    /**
     * 头条
     */
    HEADLINES(1,"头条"),
    /**
     * 新闻
     */
    NEWS(2,"新闻"),
    /**
     * 体育
     */
    SPORTS(3,"体育"),
    /**
     * 娱乐
     */
    ENTERTAINMENT(4,"娱乐"),
    /**
     * 军事
     */
    MILITARY_AFFAIRS(5,"军事"),
    /**
     * 科技
     */
    SCIENCE(6,"科技"),
    /**
     * 育儿
     */
    PARENTING(7,"育儿"),
    /**
     * 女性 其实是人情新闻
     */
    WOMAN(8,"女性");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    JuHeNewsCategoryEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }
}
