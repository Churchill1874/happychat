package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.entity.base.CreatorBaseInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("blacklist")
@ApiModel("黑名单")
public class Blacklist extends CreatorBaseInfo implements Serializable {

    private static final long serialVersionUID = -3324921096334389678L;

    @TableField("ip")
    private String ip;

    @TableField("phone")
    private String phone;

    @TableField("device")
    private String device;

    @TableField("remarks")
    private String remarks;

}
