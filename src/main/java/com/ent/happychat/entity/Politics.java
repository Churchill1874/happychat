package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("politics")
public class Politics extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -5546210780634284538L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("新闻内容")
    private String content;

    @ApiModelProperty("浏览量")
    private Integer viewCount = 0;

    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @ApiModelProperty("被评论数量")
    private Integer commentsCount = 0;

    @ApiModelProperty("新闻状态")
    private NewsStatusEnum newsStatus;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("来源")
    private String source;


}
