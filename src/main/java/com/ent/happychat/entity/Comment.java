package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.entity.base.BaseInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("comment")
@ApiModel("评论")
public class Comment extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -6413055058699812113L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("新闻id")
    private Long newsId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("顶层评论id")
    private Long topId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("回复id")
    private Long replyId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("评论人id")
    private Long playerId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("评论目标的玩家id 也就是被评论玩家id")
    private Long targetPlayerId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("信息类型")
    private InfoEnum infoType;

    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @ApiModelProperty("被评论数量")
    private Integer commentsCount = 0;

    @ApiModelProperty("读取状态")
    private Boolean readStatus;

}
