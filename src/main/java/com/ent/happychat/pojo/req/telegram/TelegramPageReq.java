// 目标路径: src/main/java/com/ent/happychat/pojo/req/telegram/TelegramPageReq.java
package com.ent.happychat.pojo.req.telegram;

import com.ent.happychat.common.constant.enums.TelegramTypeEnum;
import com.ent.happychat.pojo.req.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TelegramPageReq extends PageBase implements Serializable {
    private static final long serialVersionUID = 8526795146708710303L;

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("标题（模糊查询）")
    private String title;

    @ApiModelProperty("类型：频道/群组")
    private TelegramTypeEnum type;

    @ApiModelProperty("Telegram账号")
    private String account;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("展示状态")
    private Boolean status;

}
