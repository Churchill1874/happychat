package com.ent.happychat.pojo.resp.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PrivateChatListResp implements Serializable {
    private static final long serialVersionUID = -6955480379052382062L;

    @ApiModelProperty("登陆人账号")
    private String loginAccount;

    @ApiModelProperty("登陆人头像")
    private String loginAvatar;

    @ApiModelProperty("登陆人等级")
    private LevelEnum loginLevel;

    @ApiModelProperty("登录人名称")
    private String loginName;

    @ApiModelProperty("外层聊天记录")
    private List<PrivateChatOuterResp> list;

}
