package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("notice_board")
@ApiModel("公告")
public class NoticeBoard extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -6753582531612756290L;

    @ApiModelProperty("1通知,2打赏")
    private Integer type;

    @ApiModelProperty("内容")
    private String content;

}
