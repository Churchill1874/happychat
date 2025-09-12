package com.ent.happychat.pojo.req.tieba.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TiebaCommentAdd implements Serializable {
    private static final long serialVersionUID = -5955809877233525479L;

    @NotNull(message = "评论目标不能为空")
    @ApiModelProperty("帖子id")
    private Long tiebaId;
    @NotBlank(message = "内容不能为空")
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("回复id")
    private String replyId;
    @ApiModelProperty("图片")
    private String image;


}
