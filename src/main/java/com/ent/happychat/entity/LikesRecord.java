package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("点赞记录")
@TableName("likes_record")
public class LikesRecord extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -1585521008783241450L;

    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("点赞内容记录id")
    private Long likesId;
    @ApiModelProperty("点赞类型")
    private LikesEnum likesType;
    @ApiModelProperty("点赞内容")
    private String content;
    @ApiModelProperty("点赞目标人id 就是被点赞人")
    private Long targetPlayerId;
    @ApiModelProperty("新闻类型")
    private InfoEnum infoType;

}
