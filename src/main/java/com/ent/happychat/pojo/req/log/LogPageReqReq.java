package com.ent.happychat.pojo.req.log;

import com.ent.happychat.pojo.req.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogPageReqReq extends PageReq implements Serializable {

    private static final long serialVersionUID = -9102045578940520784L;

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

    @ApiModelProperty("平台")
    private Integer platform;

}
