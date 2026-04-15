package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("log_info")
public class LogInfo extends BaseInfo {

    @ApiModelProperty("1报错 2风控 3统计")
    private Integer type;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("ip")
    private String ip;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("用户id")
    private Long playerId;

}
