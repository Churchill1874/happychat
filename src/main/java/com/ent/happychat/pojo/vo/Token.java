package com.ent.happychat.pojo.vo;

import com.ent.happychat.common.constant.enums.UserStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Token {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime loginTime;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 状态 0禁用 1正常
     */
    private UserStatusEnum status;


}
