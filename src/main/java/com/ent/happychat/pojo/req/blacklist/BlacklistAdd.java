package com.ent.happychat.pojo.req.blacklist;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BlacklistAdd implements Serializable {

    private static final long serialVersionUID = 181783539421590989L;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("手机号")
    private String phoneNumber;

    @TableField("remarks")
    private String remarks;

}
