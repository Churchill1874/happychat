package com.ent.happychat.pojo.req.image;

import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.common.constant.enums.InfoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ImageConfigAddReq implements Serializable {
    private static final long serialVersionUID = -2226985721913415979L;

    @NotNull(message = "图片类型不能为空")
    @ApiModelProperty(value = "图片类型", required = true)
    private ImageTypeEnum imageType;

    @NotBlank(message = "路径不能为空")
    @ApiModelProperty(value = "路径", required = true)
    private String path;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    @NotNull(message = "使用状态不能为空")
    @ApiModelProperty(value = "使用状态", required = true)
    private Boolean status;

    @ApiModelProperty("新闻类型")
    private InfoEnum newsType;

    @ApiModelProperty("新闻Id")
    private Long newsId;

}
