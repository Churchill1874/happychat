package com.ent.happychat.pojo.resp.comment;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CommentPageResp extends Comment implements Serializable {
    private static final long serialVersionUID = 7192054552853170900L;

    @ApiModelProperty("新闻标题")
    private String title;

    @ApiModelProperty("评论人等级")
    private LevelEnum level;

    @ApiModelProperty("评论人名称")
    private String commentator;

    @ApiModelProperty("被评论人名称")
    private String targetPlayerName;

    @ApiModelProperty("被评论人等级")
    private LevelEnum targetPlayerLevel;

}
