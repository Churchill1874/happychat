// 目标路径: src/main/java/com/ent/happychat/common/constant/enums/TelegramTypeEnum.java
package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 电报类型：频道 / 群组
 */
public enum TelegramTypeEnum {

    CHANNEL(1, "频道"),
    GROUP(2, "群组")
    ;

    @Getter
    @EnumValue
    @JsonValue
    private int code;

    @Getter
    private String name;

    TelegramTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }

}
