// 目标路径: src/main/java/com/ent/happychat/entity/Telegram.java
package com.ent.happychat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ent.happychat.common.constant.enums.TelegramTypeEnum;
import com.ent.happychat.entity.base.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("电报频道/群组")
@TableName("telegram")
public class Telegram extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 362828520285193132L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("类型：频道/群组")
    private TelegramTypeEnum type;

    @ApiModelProperty("Telegram账号，如 @xxxx")
    private String account;

    @ApiModelProperty("跳转链接，如 https://t.me/xxxx")
    private String jumpUrl;

    @ApiModelProperty("海报图片路径（建议3:1比例）")
    private String posterImagePath;

    @ApiModelProperty("二维码图片路径，可为空")
    private String qrImagePath;

    @ApiModelProperty("详情页描述内容")
    private String description;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("展示状态")
    private Boolean status;

}
