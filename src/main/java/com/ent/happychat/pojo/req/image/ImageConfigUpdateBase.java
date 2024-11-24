package com.ent.happychat.pojo.req.image;

import com.ent.happychat.pojo.req.IdBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ImageConfigUpdateBase extends IdBase implements Serializable {
    private static final long serialVersionUID = 4940749544856105886L;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    @NotNull(message = "使用状态不能为空")
    @ApiModelProperty(value = "使用状态", required = true)
    private Boolean status;

}
