package com.ent.happychat.pojo.req.image;

import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ImageConfigAddReq implements Serializable {
    private static final long serialVersionUID = -2226985721913415979L;

    @NotNull(message = "图片类型不能为空")
    @ApiModelProperty("图片类型")
    private ImageTypeEnum imageType;

    @NotBlank(message = "路径不能为空")
    @ApiModelProperty("路径")
    private String path;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty("排序")
    private Integer sort;

    @NotNull(message = "使用状态不能为空")
    @ApiModelProperty("使用状态")
    private Boolean status;

}
