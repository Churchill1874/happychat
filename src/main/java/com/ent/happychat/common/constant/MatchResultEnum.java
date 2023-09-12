package com.ent.happychat.common.constant;

import lombok.Getter;

/**
 * 赛事结果枚举
 */
public enum MatchResultEnum {

    DRAW(0,"平局"),
    WIN(1,"主队胜"),
    LOSE(2,"主队负");

    @Getter
    private int value;

    @Getter
    private String name;

    MatchResultEnum(int value, String name){
        this.value = value;
        this.name = name;
    }


}
