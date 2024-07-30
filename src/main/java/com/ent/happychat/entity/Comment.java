package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("comment")
@ApiModel("评论")
public class Comment extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -6413055058699812113L;

    @TableField("news_id")
    @ApiModelProperty("新闻id")
    private Long newsId;

    @TableField("top_comment_id")
    @ApiModelProperty("顶层评论id")
    private Long topId;

    @TableField("reply_id")
    @ApiModelProperty("回复id")
    private Long replyId;

    @TableField("player_id")
    @ApiModelProperty("玩家id")
    private Long playerId;

    @TableField("content")
    @ApiModelProperty("评论内容")
    private String content;

    @TableField("info_type")
    @ApiModelProperty("信息类型")
    private InfoEnum infoType;

    @TableField("likes_count")
    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @TableField("comments_count")
    @ApiModelProperty("被评论数量")
    private Integer commentsCount = 0;

}
