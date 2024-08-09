package com.ent.happychat.pojo.req.likes;

import com.ent.happychat.common.constant.enums.LikesEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class LikesClickReq implements Serializable {
    private static final long serialVersionUID = -6530457917626146009L;

    @NotNull(message = "点赞内容不能为空")
    @ApiModelProperty(value = "点赞内容记录id", required = true)
    private Long likesId;
    @NotNull(message = "点赞类型不能为空")
    @ApiModelProperty(value = "点赞类型", required = true)
    private LikesEnum likesType;

}
