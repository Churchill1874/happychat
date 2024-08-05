package com.ent.happychat.pojo.req.views;

import com.ent.happychat.common.constant.enums.ViewsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ViewsAddReq implements Serializable {
    private static final long serialVersionUID = -743581582421933767L;

    @NotNull(message = "浏览内容不能为空")
    @ApiModelProperty(value = "浏览id",required = true)
    private Long viewsId;
    @NotNull(message = "浏览类型不能为空")
    @ApiModelProperty(value = "浏览类型", required = true)
    private ViewsEnum viewsType;
    @ApiModelProperty(value = "浏览内容", required = true)
    private String content;
}
