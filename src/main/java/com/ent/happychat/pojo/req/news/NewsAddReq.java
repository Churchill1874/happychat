package com.ent.happychat.pojo.req.news;

import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class NewsAddReq implements Serializable {
    private static final long serialVersionUID = -1015487095600233610L;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("来源 如果为空 后台自动默认 平台 二字")
    private String source;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty("类型")
    private NewsCategoryEnum category;

    @NotBlank(message = "图片路径不能为空")
    @ApiModelProperty("内容的图片路径")
    private String contentImagePath;

    @NotNull(message = "浏览量不能为空")
    @ApiModelProperty("浏览量")
    private Integer viewCount = 0;

    @NotNull(message = "点赞数量不能空")
    @ApiModelProperty("点赞数量")
    private Integer likesCount = 0;

    @ApiModelProperty("新闻状态 如果为空 默认为普通")
    private NewsStatusEnum newsStatus;

}
