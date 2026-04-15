package com.ent.happychat.pojo.req.login;

import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LogInfoPage extends PageBase {

    @ApiModelProperty("用户id")
    private Integer playerId;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("创建人")
    private String createName;

    @ApiModelProperty("模糊查询内容")
    private String content;

    @ApiModelProperty("1报错 2风控 3统计")
    private Integer type;

}
