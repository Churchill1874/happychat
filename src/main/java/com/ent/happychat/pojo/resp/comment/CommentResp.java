package com.ent.happychat.pojo.resp.comment;

import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentResp extends Comment implements Serializable {
    private static final long serialVersionUID = -2126204293048706283L;

    @ApiModelProperty("评论人名称")
    private String commentator;

    @ApiModelProperty("头像")
    private String avatarPath;

    @ApiModelProperty("等级")
    private LevelEnum level;

}
