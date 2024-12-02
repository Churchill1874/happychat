package com.ent.happychat.pojo.req.comment;

import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CommentPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 1244117646578208790L;

    @ApiModelProperty("新闻标题")
    private String title;

    @NotNull(message = "新闻id不能为空")
    @ApiModelProperty("新闻id")
    private Long newsId;

    @NotNull(message = "新闻来源类型不能为空")
    @ApiModelProperty("新闻类型来源")
    private InfoEnum newsType;

    @ApiModelProperty("顶层id")
    private Long topId;

    @ApiModelProperty("回复id")
    private Long replyId;

    @ApiModelProperty("评论人id")
    private Long playerId;

    @ApiModelProperty("评论目标的玩家id 也就是 被评论玩家的id")
    private Long targetPlayerId;


}
