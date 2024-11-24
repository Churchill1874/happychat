package com.ent.happychat.pojo.req.southeastasia;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SoutheastAsiaPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 8526795146708710302L;

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("新闻来源")
    private String source;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("是否热门")
    private Boolean isHot;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("展示状态")
    private Boolean status;

}
