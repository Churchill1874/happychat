package com.ent.happychat.common.constant;

import lombok.Getter;

/**
 * 比赛状态枚举
 */
public enum MatchStatusEnum {

    NOT_START(0,"未开始"),
    IN_PROGRESS(1,"比赛中"),
    FINSHED(2,"比赛结束"),
    CANCEL(3,"取消");

    @Getter
    private int value;

    @Getter
    private String name;

    MatchStatusEnum(int value, String name){
        this.value = value;
        this.name = name;
    }


}
