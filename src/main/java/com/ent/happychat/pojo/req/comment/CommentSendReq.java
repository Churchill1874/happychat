package com.ent.happychat.pojo.req.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class CommentSendReq implements Serializable {
    private static final long serialVersionUID = 4814940560283968620L;

    @NotNull(message = "缺少新闻编码id")
    @ApiModelProperty("新闻id")
    private Long newsId;

    @ApiModelProperty("顶层评论id")
    private Long topId;

    @ApiModelProperty("回复评论id")
    private Long replyId;

    @Length(max = 200, message = "每条评论最多200字")
    @NotNull(message = "请输入评论内容")
    @ApiModelProperty("评论内容")
    private String content;

}
