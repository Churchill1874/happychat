package com.ent.happychat.pojo.req.tieba;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TiebaPage extends PageBase implements Serializable {
    private static final long serialVersionUID = 1597599911769336113L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("帖子id")
    private Long id;

}
