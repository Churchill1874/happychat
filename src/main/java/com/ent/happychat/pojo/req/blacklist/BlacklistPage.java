package com.ent.happychat.pojo.req.blacklist;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BlacklistPage extends PageBase implements Serializable {

    private static final long serialVersionUID = 8526224475627286219L;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("手机号")
    private String phone;

    @TableField("device")
    private String device;


}
