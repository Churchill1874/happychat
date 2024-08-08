package com.ent.happychat.pojo.resp.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NewsCommentPageResp implements Serializable {
    private static final long serialVersionUID = 3059289223998274584L;

    @ApiModelProperty("新闻评论集合")
    private List<NewsCommentResp> list;

    @ApiModelProperty("总评论数量")
    private long commentCount = 0;

    @ApiModelProperty("点赞总数量")
    private int likesCount = 0;

    @ApiModelProperty("浏览总数量")
    private int viewsCount = 0;

}
