package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.mapper.CommentMapper;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendComment(Comment po) {
        //todo 对个人等级进行程度的数据进行更新

        //保存评论
        save(po);
    }

    @Override
    public IPage<Comment> queryTopPage(CommentPageReq po) {
        IPage<Comment> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Comment::getNewsId, po.getNewsId())
                .isNull(Comment::getTopId)
                .orderByDesc(Comment::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    public List<Comment> replyList(List<Long> topIdList) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Comment::getTopId, topIdList);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByNewsId(Long newsId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getNewsId, newsId);
        remove(queryWrapper);
    }

    @Override
    public IPage<Comment> queryPage(CommentPageReq po) {
        IPage<Comment> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(po.getNewsId() != null, Comment::getNewsId, po.getNewsId())
                .eq(po.getReplyId() != null, Comment::getReplyId, po.getReplyId())
                .ge(po.getStartTime() !=null, Comment::getCreateTime, po.getStartTime())
                .le(po.getEndTime() != null, Comment::getCreateTime, po.getEndTime())
                .orderByDesc(Comment::getCreateTime);
        return page(iPage, queryWrapper);
    }

}