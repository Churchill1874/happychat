package com.ent.happychat.pojo.resp.player;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("玩家token")
public class PlayerTokenResp implements Serializable {
    private static final long serialVersionUID = -773689514264864093L;

    @ApiModelProperty("玩家id")
    private Long id;
    @ApiModelProperty("令牌")
    private String tokenId;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("昵称")
    private String name;
    @ApiModelProperty("状态")
    private UserStatusEnum status;
    @ApiModelProperty("登陆时间")
    private LocalDateTime loginTime;
    @ApiModelProperty("头像")
    private String avatarPath;
    @ApiModelProperty("等级")
    private LevelEnum level;


}
