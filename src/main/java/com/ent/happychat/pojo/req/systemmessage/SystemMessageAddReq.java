package com.ent.happychat.pojo.req.systemmessage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SystemMessageAddReq implements Serializable {
    private static final long serialVersionUID = -5157989876537390505L;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题",required = true)
    private String title;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "消息内容",required = true)
    private String content;

    @ApiModelProperty(value = "图片路径")
    private String imagePath;

    @NotNull(message = "是否需要弹窗不能为空")
    @ApiModelProperty(value = "需要弹窗", required = true)
    private Boolean popUp;

}
