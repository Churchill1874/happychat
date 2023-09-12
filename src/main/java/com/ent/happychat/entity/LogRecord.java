package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@ApiModel("日志类")
@NoArgsConstructor
@AllArgsConstructor
@TableName("log_record")
public class LogRecord extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1887087031260287312L;

    @TableField("type")
    @ApiModelProperty("日志类型 1异常日志 2操作日志 3登录日志 4请求统计日志")
    private Integer type;

    @TableField("message")
    @ApiModelProperty("日志内容")
    private String message;

    @TableField("ip")
    @ApiModelProperty("ip地址")
    private String ip;

    @TableField("request_url")
    @ApiModelProperty("请求路径")
    private String requestUrl;

    @TableField("account")
    @ApiModelProperty("账号")
    private Integer account;

    @TableField("platform")
    @ApiModelProperty("平台")
    private Integer platform;

}
