package com.ent.happychat.pojo.req.image;

import com.ent.happychat.common.constant.enums.ImageTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImageConfigPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 1192029967398977485L;

    @ApiModelProperty("图片类型")
    private ImageTypeEnum imageType;

    @ApiModelProperty("使用状态")
    private Boolean status;

}
