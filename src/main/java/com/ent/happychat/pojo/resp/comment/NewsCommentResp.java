package com.ent.happychat.pojo.resp.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewsCommentResp implements Serializable {
    private static final long serialVersionUID = -127811981589819839L;

    @ApiModelProperty("外层评论")
    private CommentResp topComment;

    @ApiModelProperty("回复评论")
    private List<CommentResp> replyCommentList = new ArrayList<>();

}
