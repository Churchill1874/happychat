package com.ent.happychat.pojo.req.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BotCommentSend {
    @NotNull(message = "id不能为空")
    private Long id;
    private List<String> contentList;
}
