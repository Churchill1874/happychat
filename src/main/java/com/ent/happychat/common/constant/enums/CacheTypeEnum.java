package com.ent.happychat.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum CacheTypeEnum {

    PLAYER_TOKEN("player_token","玩家Token"),
    ADMIN_TOKEN("admin_token","管理员token"),
    LOCK_3_SECOND("lock_3_second","3秒锁"),
    VERIFICATION_CODE("verification_code","验证码"),
    BLACKLIST("blacklist","黑名单");

    @Getter
    @EnumValue
    @JsonValue
    private String code;

    @Getter
    private String name;

    CacheTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.code;
    }
}
