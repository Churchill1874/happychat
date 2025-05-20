package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.resp.comment.CommentResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService extends IService<Comment> {



    /**
     * 发表评论
     * @param dto
     */
    void sendComment(Comment dto);


    /**
     * 分页查询顶层评论
     * @param dto
     * @return
     */
    IPage<Comment> queryTopPage(CommentPageReq dto);

    /**
     * 回复顶层回复记录
     * @param topIdList
     * @return
     */
    List<Comment> replyList(List<Long> topIdList);


    /**
     * 根据新闻id删除新闻评论
     * @param newsId
     */
    void removeByNewsId(Long newsId);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    IPage<Comment> queryPage(CommentPageReq dto);

    /**
     * 增加评论数量
     * @param id
     */
    void increaseCommentsCount(Long id);

    /**
     * 增加点赞数量
     * @param id
     */
    boolean increaseLikesCount(Long id);


}
