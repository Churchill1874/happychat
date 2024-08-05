package com.ent.happychat.pojo.req.views;

import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ViewsRecordPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = -3049988728765972779L;

    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("浏览id")
    private Long viewsId;
    @ApiModelProperty("浏览类型")
    private ViewsEnum viewsType;
    @ApiModelProperty("浏览内容")
    private String content;

}
