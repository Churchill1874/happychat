package com.ent.happychat.service.serviceimpl;

import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.News;
import com.ent.happychat.service.CommentNewsService;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CommentNewsServiceImpl implements CommentNewsService {

    @Autowired
    private NewsService newsService;
    @Autowired
    private SystemMessageService systemMessageService;

    @Async
    @Override
    public void commentNews(Comment dto, String newsTitle, String replyContent) {
        //如果是评论的新闻类型,对新闻进行评论数量更新
        if (dto.getInfoType() == InfoEnum.NEWS) {
            newsService.increaseCommentsCount(dto.getNewsId());
        }

        //异步发送回复评论系统消息
        systemMessageService.sendCommentMessage(dto, newsTitle, replyContent);
    }

}
