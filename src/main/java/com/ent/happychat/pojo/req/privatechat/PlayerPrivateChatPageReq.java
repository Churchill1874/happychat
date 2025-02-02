package com.ent.happychat.pojo.req.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class PlayerPrivateChatPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = -5290606037007891778L;

    @ApiModelProperty("账号a 用于查询指定的对方玩家账号")
    private String accountA;
    @ApiModelProperty("账号b")
    private String accountB;

    @ApiModelProperty("发送人名字")
    private String accountAName;
    @ApiModelProperty("接收人名字")
    private String accountBName;

    @ApiModelProperty("发送人头像路径")
    private String accountAAvatarPath;
    @ApiModelProperty("接收人头像路径")
    private String accountBAvatarPath;

    @ApiModelProperty("发送人等级")
    private LevelEnum accountALevel;
    @ApiModelProperty("接收人等级")
    private LevelEnum accountBLevel;


}
