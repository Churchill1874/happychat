package com.ent.happychat.pojo.resp.likes;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.LikesRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LikesRecordPageResp extends LikesRecord implements Serializable {
    private static final long serialVersionUID = -5346998088432867309L;

    @ApiModelProperty("玩家名称")
    private String playerName;
    @ApiModelProperty("玩家账号")
    private String account;
    @ApiModelProperty("等级")
    private LevelEnum level;
    @ApiModelProperty("头像路径")
    private String avatarPath;
}
