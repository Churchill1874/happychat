package com.ent.happychat.pojo.req.log;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogListReq implements Serializable {

    private static final long serialVersionUID = 5831095486858078842L;

    @ApiModelProperty("日志类型 1异常日志 2操作日志 3登录日志 4请求统计日志")
    private Integer type;

    @ApiModelProperty("日志内容")
    private String message;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("请求路径")
    private String requestUrl;

    @ApiModelProperty("账号")
    private Integer account;

}
