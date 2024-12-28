package com.ent.happychat.pojo.resp.systemmessage;

import com.ent.happychat.entity.SystemMessage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemMessageResp extends SystemMessage implements Serializable {
    private static final long serialVersionUID = -6883029997490006776L;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("评论人名称")
    private String senderName;

}
