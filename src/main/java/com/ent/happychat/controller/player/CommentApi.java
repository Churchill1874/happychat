package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.req.comment.CommentSendReq;
import com.ent.happychat.pojo.resp.BooleanResp;
import com.ent.happychat.pojo.resp.comment.CommentResp;
import com.ent.happychat.pojo.resp.comment.NewsCommentPageResp;
import com.ent.happychat.pojo.resp.comment.NewsCommentResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.EhcacheService;
import com.ent.happychat.service.PlayerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "评论")
@RequestMapping("/player/comment")
public class CommentApi {

    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PlayerInfoService playerInfoService;


    @PostMapping("/findNewsComments")
    @ApiOperation(value = "根据新闻id分页查询新闻评论", notes = "根据新闻id分页查询新闻评论")
    public R<NewsCommentPageResp> findNewsComments(@RequestBody @Valid CommentPageReq req) {
        if (req.getNewsId() == null) {
            throw new DataException("搜索新闻数据异常");
        }

        //待返回的新闻评论对象
        NewsCommentPageResp newsCommentRespPage = new NewsCommentPageResp();

        IPage<Comment> topPage = null;
        //如果需要新闻评论位置定位查询
        if (req.getNeedCommentPoint()){
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Comment::getNewsId, req.getNewsId());
            List<Comment> list = commentService.list(queryWrapper);
            topPage = new Page<>(1, list.size());
            topPage.setTotal(list.size());
            topPage.setRecords(list);
        } else {
            //否则就正常分页
            //查顶部新闻评论分页
            CommentPageReq commentPageReq = new CommentPageReq();
            commentPageReq.setNewsId(req.getNewsId());
            commentPageReq.setNewsType(req.getNewsType());
            commentPageReq.setPageNum(req.getPageNum());
            commentPageReq.setPageSize(req.getPageSize());
            topPage = commentService.queryTopPage(commentPageReq);
        }

        if (CollectionUtils.isEmpty(topPage.getRecords())) {
            return R.ok(newsCommentRespPage);
        }


        //所有 顶层评论 和 回复评论 涉及到的用户id
        Set<Long> playerIdSet = new HashSet<>();
        //根据分页出来的顶层评论记录id 找到每个评论下面的回复记录
        List<Long> topIdList = new ArrayList<>();

        topPage.getRecords().forEach(comment -> {
            playerIdSet.add(comment.getPlayerId());
            topIdList.add(comment.getId());
        });

        //该新闻所有顶层评论下的回复记录
        List<Comment> replyList = commentService.replyList(topIdList);
        //根据顶层评论id分组
        Map<Long, List<Comment>> replyGroup = new HashMap<>();
        if (CollectionUtils.isNotEmpty(replyList)) {
            //将所有回复评论的用户id也放入用户id集合
            playerIdSet.addAll(replyList.stream().map(Comment::getPlayerId).collect(Collectors.toSet()));
            replyGroup = replyList.stream().collect(Collectors.groupingBy(Comment::getTopId));
        }


        //查询出所有该新闻评论人的信息
        Map<Long, PlayerInfo> playerInfoMap = playerInfoService.mapByIds(new ArrayList<>(playerIdSet));


        //以顶层评论为单位 每组评论集合
        List<NewsCommentResp> list = new ArrayList<>();
        for (Comment top : topPage.getRecords()) {
            //获取顶层评论人的信息
            PlayerInfo topPlayer = playerInfoMap.get(top.getPlayerId());
            NewsCommentResp newsCommentResp = new NewsCommentResp();
            CommentResp topResp = BeanUtil.toBean(top, CommentResp.class);
            topResp.setCommentator(topPlayer.getName());
            topResp.setAvatarPath(topPlayer.getAvatarPath());
            topResp.setLevel(topPlayer.getLevel());
            newsCommentResp.setTopComment(topResp);

            //存入该顶层评论的回复记录
            List<Comment> replyListOfTop = replyGroup.get(topResp.getId());
            if (CollectionUtils.isNotEmpty(replyListOfTop)) {
                List<CommentResp> replyRespListOfTop = new ArrayList<>();
                for (Comment reply : replyListOfTop) {
                    PlayerInfo replyPlayer = playerInfoMap.get(reply.getPlayerId());
                    CommentResp replyResp = BeanUtil.toBean(reply, CommentResp.class);
                    replyResp.setCommentator(replyPlayer.getName());
                    replyResp.setAvatarPath(replyPlayer.getAvatarPath());
                    replyResp.setLevel(replyPlayer.getLevel());
                    replyRespListOfTop.add(replyResp);
                }
                newsCommentResp.setReplyCommentList(replyRespListOfTop);
            }


            list.add(newsCommentResp);
        }

        //存入该新闻 顶层和回复的评论组 列表
        newsCommentRespPage.setList(list);
        return R.ok(newsCommentRespPage);
    }

    @PostMapping("/sendNewsComment")
    @ApiOperation(value = "发表新闻评论", notes = "发表新闻评论")
    public R<CommentResp> sendNewsComment(@RequestBody @Valid CommentSendReq req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        ehcacheService.verification3SecondsRequest(playerTokenResp.getAccount());

        if (req.getTopId() == null && req.getReplyId() != null) {
            log.warn("评论缺少顶层评论id:{}", JSONObject.toJSONString(req));
            throw new DataException("顶层评论不存在或已删除");
        }


        Comment comment = new Comment();
        comment.setNewsId(req.getNewsId());
        comment.setTopId(req.getTopId());
        comment.setReplyId(req.getReplyId());
        comment.setPlayerId(playerTokenResp.getId());
        comment.setInfoType(InfoEnum.NEWS);
        comment.setCreateTime(LocalDateTime.now());
        comment.setCreateName(playerTokenResp.getName());
        comment.setContent(req.getContent());
        comment.setReadStatus(false);
        commentService.sendComment(comment);

        CommentResp commentResp = BeanUtil.toBean(comment, CommentResp.class);
        commentResp.setAvatarPath(playerTokenResp.getAvatarPath());
        commentResp.setCommentator(playerTokenResp.getName());
        commentResp.setLevel(playerTokenResp.getLevel());
        return R.ok(commentResp);
    }

    @PostMapping("/receivedCommentsPage")
    @ApiOperation(value = "分页查看收到的评论", notes = "分页查看收到的评论")
    public R<List<CommentResp>> receivedCommentsPage(@RequestBody @Valid PageBase req) {
        CommentPageReq commentPageReq = new CommentPageReq();
        commentPageReq.setPageNum(req.getPageNum());
        commentPageReq.setPageSize(req.getPageSize());
        commentPageReq.setTargetPlayerId(TokenTools.getPlayerToken(true).getId());
        IPage<Comment> commentPage = commentService.queryPage(commentPageReq);

        //如果没有收到过任何的评论
        if (CollectionUtils.isEmpty(commentPage.getRecords())) {
            return R.ok(null);
        }

        //获取所有对自己评论的玩家id集合
        List<Long> idList = commentPage.getRecords().stream().map(Comment::getPlayerId).collect(Collectors.toList());
        //查询所有评论自己的玩家信息集合
        Map<Long, PlayerInfo> playerInfoMap = playerInfoService.mapByIds(idList);

        List<CommentResp> list = new ArrayList<>();
        commentPage.getRecords().forEach(comment -> {
            PlayerInfo commentator = playerInfoMap.get(comment.getPlayerId());
            CommentResp commentResp = BeanUtil.toBean(comment, CommentResp.class);
            commentResp.setCommentator(commentator.getName());
            commentResp.setAvatarPath(commentator.getAvatarPath());
            commentResp.setLevel(commentator.getLevel());
            list.add(commentResp);
        });

        return R.ok(list);
    }

    @PostMapping("/increaseLikesCount")
    @ApiOperation(value = "点赞评论", notes = "点赞评论")
    public R<BooleanResp> increaseLikesCount(@RequestBody @Valid IdBase req) {
        BooleanResp booleanResp = new BooleanResp();
        booleanResp.setValue(commentService.increaseLikesCount(req.getId()));
        return R.ok(booleanResp);
    }

}
































