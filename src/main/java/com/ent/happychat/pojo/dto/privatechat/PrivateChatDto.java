package com.ent.happychat.pojo.dto.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateChatDto implements Serializable {

    //账号
    private String account;
    //名称
    private String name;
    //头像
    private String avatarPath;
    //等级
    private LevelEnum level;

}
