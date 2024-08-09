package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("浏览记录")
@TableName("views_record")
public class ViewsRecord extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -4669804871478298287L;

    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("浏览id")
    private Long viewsId;
    @ApiModelProperty("ip地址")
    private String ip;
    @ApiModelProperty("浏览类型")
    private ViewsEnum viewsType;
    @ApiModelProperty("浏览内容")
    private String content;

}
