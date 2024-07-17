package com.ent.happychat.pojo.req.image;

import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ImageConfigPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 1192029967398977485L;

    @NotNull(message = "图片类型不能为空")
    @ApiModelProperty("图片类型")
    private ImageTypeEnum imageType;

    @NotNull(message = "使用状态不能为空")
    @ApiModelProperty("使用状态")
    private Boolean status;

}
