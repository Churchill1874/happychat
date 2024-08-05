package com.ent.happychat.pojo.req.likes;

import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LikesRecordPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 7628095020859198150L;

    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("点赞内容记录id")
    private Long likesId;
    @ApiModelProperty("点赞类型")
    private LikesEnum likesType;
    @ApiModelProperty("点赞内容")
    private String content;

}
