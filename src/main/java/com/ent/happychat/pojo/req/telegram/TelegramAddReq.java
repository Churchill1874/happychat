// 目标路径: src/main/java/com/ent/happychat/pojo/req/telegram/TelegramAddReq.java
package com.ent.happychat.pojo.req.telegram;

import com.ent.happychat.common.constant.enums.TelegramTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TelegramAddReq implements Serializable {
    private static final long serialVersionUID = 895133427848543842L;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：频道/群组", required = true)
    private TelegramTypeEnum type;

    @NotBlank(message = "Telegram账号不能为空")
    @ApiModelProperty(value = "Telegram账号，如 @xxxx", required = true)
    private String account;

    @NotBlank(message = "跳转链接不能为空")
    @ApiModelProperty(value = "跳转链接，如 https://t.me/xxxx", required = true)
    private String jumpUrl;

    @NotBlank(message = "海报图片不能为空")
    @ApiModelProperty(value = "海报图片路径（建议3:1比例）", required = true)
    private String posterImagePath;

    @ApiModelProperty("二维码图片路径，可为空")
    private String qrImagePath;

    @NotBlank(message = "描述内容不能为空")
    @ApiModelProperty(value = "详情页描述内容", required = true)
    private String description;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("展示状态")
    private Boolean status;

}
