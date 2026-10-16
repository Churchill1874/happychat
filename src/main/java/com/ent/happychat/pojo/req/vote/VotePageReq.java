package com.ent.happychat.pojo.req.vote;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class VotePageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = -2003963622402316722L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("标题模糊搜索")
    private String title;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("展示状态")
    private Boolean status;

    @ApiModelProperty("是否已截止 true已截止 false进行中")
    private Boolean ended;

}
