package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.News;
import com.ent.happychat.mapper.CommentMapper;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.PlayerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private NewsService newsService;
    @Autowired
    private LikesRecordService likesRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendComment(Comment po) {
        //处理评论的内容
        //如果topId 也是空 那证明当前评论就顶层评论 什么都不用做

        //如果评论的是顶层评论
        //有顶层评论Id 但是没有内嵌评论id 那发布的就是对顶层评论的评论
        if (po.getTopId() != null && po.getReplyId() == null) {
            Comment targetComment = getById(po.getTopId());
            if (targetComment == null) {
                throw new DataException("您评论的内容已删除或不存在");
            }
            po.setTargetPlayerId(targetComment.getPlayerId());
        }

        //如果评论的是内嵌的评论
        //有顶层评论 也有内嵌评论id 发布的就是对内嵌评论的评论
        if (po.getTopId() != null && po.getReplyId() != null) {
            Comment targetComment = getById(po.getReplyId());
            if (targetComment == null) {
                throw new DataException("您评论的内容已删除或不存在");
            }
            po.setContent(String.format("回复 %s: %s", targetComment.getCreateName(), po.getContent()));
            po.setTargetPlayerId(targetComment.getPlayerId());
        }

        //如果是评论的新闻类型,对新闻进行评论数量更新
        if (po.getInfoType() == InfoEnum.NEWS) {
            newsService.increaseCommentsCount(po.getNewsId());
        }

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
        queryWrapper.lambda()
                .in(Comment::getTopId, topIdList)
                .orderByDesc(Comment::getCreateTime);
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
        if (StringUtils.isNotBlank(po.getTitle())) {
            QueryWrapper<News> newsQuery = new QueryWrapper<>();
            newsQuery.lambda().eq(News::getTitle, po.getTitle());
            News news = newsService.getOne(newsQuery);
            if (po.getNewsId() != null && po.getNewsId().compareTo(news.getId()) != 0){
                throw new DataException("您搜索的新闻id和新闻标题归属的新闻记录id不一致");
            }
            po.setNewsId(news.getId());
        }

        IPage<Comment> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(po.getNewsId() != null, Comment::getNewsId, po.getNewsId())
                .eq(po.getReplyId() != null, Comment::getReplyId, po.getReplyId())
                .eq(po.getPlayerId() != null, Comment::getPlayerId, po.getPlayerId())
                .eq(po.getTargetPlayerId() != null, Comment::getTargetPlayerId, po.getTargetPlayerId())
                .ge(po.getStartTime() != null, Comment::getCreateTime, po.getStartTime())
                .le(po.getEndTime() != null, Comment::getCreateTime, po.getEndTime())
                .orderByDesc(Comment::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Async
    @Override
    public void increaseCommentsCount(Long id) {
        //增加评论次数
        baseMapper.increaseCommentsCount(id);
    }

    @Override
    public Boolean increaseLikesCount(Long id) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        Comment comment = getById(id);
        if (comment == null){
            throw new DataException("内容不存在或已删除");
        }
        QueryWrapper<LikesRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(LikesRecord::getPlayerId, playerTokenResp.getId())
                .eq(LikesRecord::getLikesId, id)
                .eq(LikesRecord::getLikesType, LikesEnum.COMMENT);
        int count = likesRecordService.count(queryWrapper);
        //增加点赞次数
        if (count == 0){
            likesRecordService.increaseLikesCount(
                    playerTokenResp.getId(),
                    playerTokenResp.getName(),
                    id,
                    comment.getContent(),
                    LikesEnum.COMMENT,
                    comment.getTargetPlayerId()
            );

            baseMapper.increaseLikesCount(id);
            return true;
        } else {
            return null;
        }


    }


}