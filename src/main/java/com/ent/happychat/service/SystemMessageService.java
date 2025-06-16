package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.SystemMessage;
import com.ent.happychat.pojo.req.systemmessage.SystemMessageAddReq;
import com.ent.happychat.pojo.req.systemmessage.SystemMessagePageReq;

public interface SystemMessageService extends IService<SystemMessage> {

    IPage<SystemMessage> queryPage(SystemMessagePageReq dto);

    /**
     * 发送系统消息
     * @param dto
     */
    void sendSystemMessage(SystemMessageAddReq dto);

    /**
     * 解析发送回复评论的系统消息
     * @param dto
     */
    void sendCommentMessage(Comment dto, String newsTitle, String replyComment);

    /**
     * 发送互动消息
     * @param  senderId,
     * @recipientId,
     * @param title,
     * @param content
     */
    void sendInteractiveMessage(Long senderId, Long recipientId, String title, String content);

    /**
     * 清除所有未读状态
     * @param playerId
     */
    void readAll(Long playerId);

}
