package com.ent.happychat.pojo.req.player;

import com.ent.happychat.pojo.req.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlayerPage extends Page implements Serializable {

    private static final long serialVersionUID = -4795834215061216103L;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("账号")
    private Integer account;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("状态 1正常 0禁用")
    private Integer status;


}
