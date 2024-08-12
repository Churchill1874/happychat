package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Churchill
 */
public enum LevelEnum {
    LEVEL_0(0,"暗中观察",0,0),
    LEVEL_1(1,"新人小白", 10,0),
    LEVEL_2(2,"围观群众",100,0),
    LEVEL_3(3,"热心市民",1000,0),
    LEVEL_4(4,"民间导师",10000,0),
    LEVEL_5(5,"江湖高人",100000,0),
    LEVEL_6(6,"阅历长者",500000,1),
    LEVEL_7(7,"社会精英",1000000,10),
    LEVEL_8(8,"政客",5000000,200),
    LEVEL_9(9,"智囊",10000000,300),
    LEVEL_10(10,"谋士",20000000,500),
    LEVEL_11(11,"军师",30000000,1000),
    LEVEL_12(12,"领袖",100000000,5000);

    /**
     *级别编码
     */
    @Getter
    @EnumValue
    @JsonValue
    private int code;

    /**
     * 名称
     */
    @Getter
    private String name;

    /**
     *评论被点赞次数
     */
    @Getter
    private int likesReceivedCount;

    /**
     *新闻正确下注猜对结果次数
     */
    @Getter
    private int correctCount;

    LevelEnum(int code, String name, int likesReceivedCount, int correctCount) {
        this.code = code;
        this.name = name;
        this.likesReceivedCount = likesReceivedCount;
        this.correctCount = correctCount;
    }

    public static LevelEnum nextLevel(LevelEnum currentLevel){
        int nextLevelCode = currentLevel.getCode() + 1;
        if (nextLevelCode >= 12){
            return currentLevel;
        }
        return findByCode(nextLevelCode);
    }

    public static LevelEnum findByCode(int code){
        for(LevelEnum level: LevelEnum.values()){
            if (level.getCode() == code){
                return level;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

    @Data
    public static class LevelType implements Serializable {
        private static final long serialVersionUID = -2379684223221026388L;
        @ApiModelProperty("等级")
        private int code;
        @ApiModelProperty("等级名称")
        private String name;
        @ApiModelProperty("被赞数量")
        private int likesReceivedCount;
        @ApiModelProperty("下注猜中数量")
        private int correctCount;
    }

}
