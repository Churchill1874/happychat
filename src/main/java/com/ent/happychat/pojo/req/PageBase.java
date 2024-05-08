package com.ent.happychat.pojo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PageBase implements Serializable {

    private static final long serialVersionUID = -8103264702679433595L;

    @ApiModelProperty(value = "页号", required = true)
    @NotNull(message = "分页页号不能为空")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "数据长度", required = true)
    @NotNull(message = "分页数据长度不能为空")
    private Integer pageSize = 10;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
