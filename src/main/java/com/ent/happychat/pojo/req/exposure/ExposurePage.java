package com.ent.happychat.pojo.req.exposure;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExposurePage extends PageBase implements Serializable {
    private static final long serialVersionUID = 2079020509939597140L;

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("是否置顶")
    private Boolean isTop;
    @ApiModelProperty("等级")
    private Integer level;
    @ApiModelProperty("事件地点")
    private String address;

}
