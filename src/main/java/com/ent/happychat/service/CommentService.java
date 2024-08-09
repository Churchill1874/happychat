package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService extends IService<Comment> {



    /**
     * 发表评论
     * @param po
     */
    void sendComment(Comment po);


    /**
     * 分页查询顶层评论
     * @param po
     * @return
     */
    IPage<Comment> queryTopPage(CommentPageReq po);

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
     * @param po
     * @return
     */
    IPage<Comment> queryPage(CommentPageReq po);

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
