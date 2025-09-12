package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tieba_comment")
@ApiModel("贴吧评论")
public class TiebaComment extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 302446012508100398L;

    @ApiModelProperty("帖子id")
    private Long tiebaId;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("等级")
    private LevelEnum level;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("回复id")
    private String replyId;
    @ApiModelProperty("回复内容")
    private String replyContent;
    @ApiModelProperty("图片")
    private String image;
    @ApiModelProperty("点赞数量")
    private Integer likesCount;

}
