package com.ent.happychat.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Page implements Serializable {

    private static final long serialVersionUID = -8103264702679433595L;

    @ApiModelProperty("页号")
    @NotNull(message = "分页页号不能为空")
    private Integer pageNum;

    @ApiModelProperty("数据长度")
    @NotNull(message = "分页数据长度不能为空")
    private Integer pageSize;

}
