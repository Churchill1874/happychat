package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.common.constant.enums.SystemNoticeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.*;
import com.ent.happychat.mapper.CommentMapper;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private NewsService newsService;
    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private CommentNewsService commentNewsService;
    @Autowired
    private PoliticsService politicsService;
    @Autowired
    private SoutheastAsiaService southeastAsiaService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SocietyService societyService;
    @Autowired
    private InteractiveStatisticsService interactiveStatisticsService;
    @Autowired
    private SystemMessageService systemMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendComment(Comment dto) {
        //处理评论的内容
        //如果topId 也是空 那证明当前评论就顶层评论 什么都不做暂时

        //如果评论的是顶层评论
        //有顶层评论Id 但是没有内嵌评论id 那发布的就是对顶层评论的评论
        if (dto.getTopId() != null && dto.getReplyId() == null) {
            Comment targetComment = getById(dto.getTopId());
            if (targetComment == null) {
                throw new DataException("您评论的内容已删除或不存在");
            }
            dto.setTargetPlayerId(targetComment.getPlayerId());
        }

        //如果评论的是内嵌的评论
        //有顶层评论 也有内嵌评论id 发布的就是对内嵌评论的评论
        if (dto.getTopId() != null && dto.getReplyId() != null) {
            Comment targetComment = getById(dto.getReplyId());
            if (targetComment == null) {
                throw new DataException("您评论的内容已删除或不存在");
            }
            dto.setContent(String.format("回复 %s: %s", targetComment.getCreateName(), dto.getContent()));
            dto.setTargetPlayerId(targetComment.getPlayerId());
        }

        //保存评论
        save(dto);

        String replyContent = null;
        Comment comment = null;
        if (dto.getTopId() != null && dto.getReplyId() == null) {
            comment = getById(dto.getTopId());
        }
        if (dto.getTopId() != null && dto.getReplyId() != null) {
            comment = getById(dto.getReplyId());
        }
        if (comment != null) {
            replyContent = comment.getContent();
        }

        String title = null;
        if (dto.getInfoType() == InfoEnum.NEWS) {
            News news = newsService.getById(dto.getNewsId());
            if (news != null) {
                title = news.getTitle();
            }
        }
        if (dto.getInfoType() == InfoEnum.POLITICS) {
            Politics politics = politicsService.getById(dto.getNewsId());
            if (politics != null) {
                title = politics.getTitle();
            }
        }
        if (dto.getInfoType() == InfoEnum.SOUTHEAST_ASIA) {
            SoutheastAsia southeastAsia = southeastAsiaService.getById(dto.getNewsId());
            if (southeastAsia != null) {
                title = southeastAsia.getTitle();
            }
        }
        if (dto.getInfoType() == InfoEnum.PROMOTION) {
            Promotion promotion = promotionService.getById(dto.getNewsId());
            if (promotion != null) {
                title = promotion.getTitle();
            }
        }
        if (dto.getInfoType() == InfoEnum.SOCIETY){
            Society society = societyService.getById(dto.getNewsId());
            if (society != null){
                title = society.getTitle();
            }
        }
        if (dto.getInfoType() == InfoEnum.TOPIC){
            Topic topic = topicService.getById(dto.getNewsId());
            if (topic != null){
                title = topic.getTitle();
            }
        }

        commentNewsService.commentNews(dto, title, replyContent);
    }


    @Override
    public IPage<Comment> queryTopPage(CommentPageReq dto) {
        IPage<Comment> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(Comment::getNewsId, dto.getNewsId())
            .eq(Comment::getInfoType, dto.getNewsType())
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
    public IPage<Comment> queryPage(CommentPageReq dto) {
        if (StringUtils.isNotBlank(dto.getTitle())) {
            QueryWrapper<News> newsQuery = new QueryWrapper<>();
            newsQuery.lambda().eq(News::getTitle, dto.getTitle());
            News news = newsService.getOne(newsQuery);
            if (dto.getNewsId() != null && dto.getNewsId().compareTo(news.getId()) != 0) {
                throw new DataException("您搜索的新闻id和新闻标题归属的新闻记录id不一致");
            }
            dto.setNewsId(news.getId());
        }

        IPage<Comment> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(dto.getNewsType() != null, Comment::getInfoType, dto.getNewsType())
            .eq(dto.getNewsId() != null, Comment::getNewsId, dto.getNewsId())
            .eq(dto.getReplyId() != null, Comment::getReplyId, dto.getReplyId())
            .eq(dto.getPlayerId() != null, Comment::getPlayerId, dto.getPlayerId())
            .eq(dto.getTargetPlayerId() != null, Comment::getTargetPlayerId, dto.getTargetPlayerId())
            .ge(dto.getStartTime() != null, Comment::getCreateTime, dto.getStartTime())
            .le(dto.getEndTime() != null, Comment::getCreateTime, dto.getEndTime())
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
    public boolean increaseLikesCount(Long id) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        Comment comment = getById(id);
        if (comment == null) {
            throw new DataException("内容不存在或已删除");
        }
        QueryWrapper<LikesRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(LikesRecord::getPlayerId, playerTokenResp.getId())
            .eq(LikesRecord::getLikesId, id)
            .eq(LikesRecord::getLikesType, LikesEnum.COMMENT);
        int count = likesRecordService.count(queryWrapper);
        //增加点赞次数
        if (count == 0) {
            likesRecordService.increaseLikesCount(
                playerTokenResp.getId(),
                playerTokenResp.getName(),
                id,
                comment.getContent(),
                LikesEnum.COMMENT,
                comment.getPlayerId(),
                comment.getInfoType()
            );

            baseMapper.increaseLikesCount(id);
            interactiveStatisticsService.addLikesReceived(comment.getPlayerId());

            String content = comment.getContent();
            //如果评论内容大于12个字符 只保留12个字符就够了
            if (StringUtils.isNotBlank(content) && content.length() > 12){
                content = "点赞了您的评论:" + content.substring(0, 12) + "...";
            }
            systemMessageService.sendInteractiveMessage(
                playerTokenResp.getId(),
                comment.getPlayerId(),
                "您收到了 " + playerTokenResp.getName() + " 的点赞",
                content,
                SystemNoticeEnum.LIKES
            );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int unreadCount(Long receiveId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(Comment::getTargetPlayerId, receiveId)
            .eq(Comment::getReadStatus, false);
        return count(queryWrapper);
    }


}