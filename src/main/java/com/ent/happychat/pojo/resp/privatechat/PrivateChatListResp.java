package com.ent.happychat.pojo.resp.privatechat;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PrivateChatListResp implements Serializable {
    private static final long serialVersionUID = -6955480379052382062L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("登陆人Id")
    private Long loginId;

    @ApiModelProperty("登陆人头像")
    private String loginAvatar;

    @ApiModelProperty("登陆人等级")
    private LevelEnum loginLevel;

    @ApiModelProperty("登录人名称")
    private String loginName;

    @ApiModelProperty("外层聊天记录")
    private List<PrivateChatOuterResp> list;

}
