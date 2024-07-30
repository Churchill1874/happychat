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
    private int count = 0;

}
