package com.ent.happychat.pojo.req.promotion;

import com.ent.happychat.common.constant.enums.PromotionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PromotionAddReq implements Serializable {
    private static final long serialVersionUID = 895133427848543841L;

    @ApiModelProperty(value = "类型",required = true)
    private PromotionTypeEnum type;

    @NotBlank(message = "描述内容不能为空")
    @ApiModelProperty(value = "内容",required = true)
    private String content;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("阅读数量")
    private Integer viewCount;

    @ApiModelProperty("评论数量")
    private Integer commentsCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @NotBlank(message = "联系方式不能为空")
    @Length(max = 50, message = "最多50字长度")
    @ApiModelProperty(value = "联系方式 最多50字符",required = true)
    private String contact;

    @NotBlank(message = "区域不能为空")
    @ApiModelProperty(value = "区域",required = true)
    private String area;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("视频路径")
    private String videoPath;

    @ApiModelProperty("价格描述")
    private String price;

}
