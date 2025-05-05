package com.ent.happychat.service;

import com.ent.happychat.entity.Comment;

/**
 * 评论相关的业务汇总接口
 */
public interface CommentNewsService {

    public void commentNews(Comment dto, String newsTitle, String replyContent);

}
