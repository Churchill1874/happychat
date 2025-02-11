package com.ent.happychat.pojo.dto.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatPersonInfoDto implements Serializable {

    private static final long serialVersionUID = 4330009566367155752L;

    //账号
    private String account;
    //名称
    private String name;
    //头像
    private String avatarPath;
    //等级
    private LevelEnum level;

}
