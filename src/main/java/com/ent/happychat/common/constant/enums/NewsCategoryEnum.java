package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

//国内平台新闻类型枚举
public enum NewsCategoryEnum {

    News(1,"新闻"),
    SPORTS(2,"体育"),
    ENTERTAINMENT(3,"娱乐"),
    MILITARY_AFFAIRS(4,"军事"),
    SCIENCE(5,"科技"),
    FAVOR(6,"人情"),
    PLAYER(7,"网友");

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    NewsCategoryEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

    //转换聚合新闻类型到平台新闻类型枚举
    public static NewsCategoryEnum convertJuHeNewsType(JuHeNewsCategoryEnum juHeNewsCategoryEnum){
        if (JuHeNewsCategoryEnum.NEWS == juHeNewsCategoryEnum || JuHeNewsCategoryEnum.HEADLINES == juHeNewsCategoryEnum){
            return NewsCategoryEnum.News;
        }
        if (JuHeNewsCategoryEnum.SPORTS == juHeNewsCategoryEnum){
            return NewsCategoryEnum.SPORTS;
        }
        if (JuHeNewsCategoryEnum.ENTERTAINMENT == juHeNewsCategoryEnum){
            return NewsCategoryEnum.ENTERTAINMENT;
        }
        if (JuHeNewsCategoryEnum.MILITARY_AFFAIRS == juHeNewsCategoryEnum){
            return NewsCategoryEnum.MILITARY_AFFAIRS;
        }
        if (JuHeNewsCategoryEnum.SCIENCE == juHeNewsCategoryEnum){
            return NewsCategoryEnum.SCIENCE;
        }
        if (JuHeNewsCategoryEnum.WOMAN == juHeNewsCategoryEnum){
            return NewsCategoryEnum.FAVOR;
        }
        return null;
    }

}
