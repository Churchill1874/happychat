package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum LevelTypeEnum {
    LEVEL_0(0,"暗中观察",0,0,0),
    LEVEL_1(1,"新人小白",1, 10,0),
    LEVEL_2(2,"围观群众",50,100,0),
    LEVEL_3(3,"热心市民",100,1000,0),
    LEVEL_4(4,"阅历长者",200,10000,0),
    LEVEL_5(5,"民间导师",300,100000,0),
    LEVEL_6(6,"江湖高人",500,500000,1),
    LEVEL_7(7,"社会精英",500,1000000,10),
    LEVEL_8(8,"名宿",800,3000000,100),
    LEVEL_9(9,"政客",1000,5000000,200),
    LEVEL_10(10,"智囊",2000,10000000,300),
    LEVEL_11(11,"谋士",3000,20000000,500),
    LEVEL_12(12,"军师",3000,30000000,1000),
    LEVEL_13(13,"帝师",3000,40000000,2000),
    LEVEL_14(14,"王佐",3000,50000000,3000),
    LEVEL_15(15,"领袖",10000,100000000,5000);

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    //新闻评论次数
    @Getter
    private int commentCount;

    //评论被点赞次数
    @Getter
    private int likesReceivedCount;

    //新闻正确下注猜对结果次数
    @Getter
    private int correctCount;

    LevelTypeEnum(int code, String name, int commentCount, int likesReceivedCount, int correctCount) {
        this.code = code;
        this.name = name;
        this.commentCount = commentCount;
        this.likesReceivedCount = likesReceivedCount;
        this.correctCount = correctCount;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
