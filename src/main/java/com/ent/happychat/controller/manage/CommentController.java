package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.entity.*;
import com.ent.happychat.pojo.req.comment.BotCommentSend;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.resp.comment.CommentPageResp;
import com.ent.happychat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "评论管理")
@RequestMapping("/manage/comment")
public class CommentController {

    @Autowired
    private PoliticsService politicsService;
    @Autowired
    private SocietyService societyService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SoutheastAsiaService southeastAsiaService;
    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;


    @AdminLoginCheck
    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<CommentPageResp>> queryPage(@RequestBody @Valid CommentPageReq req) {
        IPage<CommentPageResp> commentPageRespPage = new Page<>(req.getPageNum(), req.getPageSize());

        IPage<Comment> commentPage = commentService.queryPage(req);

        if (CollectionUtils.isEmpty(commentPage.getRecords())) {
            return R.ok(commentPageRespPage);
        }

        //获取 新闻 评论人 和 被评论人的map结构结合数据 方便拼装
        Map<Long, News> newsMap = new HashMap<>();
        Map<Long, SoutheastAsia> southeastAsiaMap = new HashMap<>();
        Map<Long, Topic> topicMap = new HashMap<>();
        Map<Long, Society> societyMap = new HashMap<>();
        Map<Long, Politics> politicsMap = new HashMap<>();

        newsData(commentPage.getRecords(), newsMap, southeastAsiaMap, topicMap, societyMap, politicsMap);

        List<Long> playerIdList = commentPage.getRecords().stream().map(Comment::getPlayerId).collect(Collectors.toList());
        List<Long> targetPlayerIdList = commentPage.getRecords().stream().map(Comment::getTargetPlayerId).filter(Objects::nonNull).collect(Collectors.toList());
        playerIdList.addAll(targetPlayerIdList);
        Map<Long, PlayerInfo> playerInfoMap = playerInfoService.mapByIds(playerIdList);


        List<CommentPageResp> commentPageRespList = new ArrayList<>();
        for (Comment comment : commentPage.getRecords()) {
            PlayerInfo player = playerInfoMap.get(comment.getPlayerId());

            CommentPageResp commentPageResp = BeanUtil.toBean(comment, CommentPageResp.class);
            commentPageResp.setCommentator(player.getName());
            commentPageResp.setTitle(getTitle(comment, newsMap, southeastAsiaMap, topicMap, societyMap, politicsMap));
            commentPageResp.setLevel(player.getLevel());

            if (comment.getTargetPlayerId() != null) {
                PlayerInfo targetPlayer = playerInfoMap.get(comment.getTargetPlayerId());
                if (targetPlayer == null) {
                    throw new DataException(String.format("未找到被评论人信息,targetPlayerId:%s", comment.getTargetPlayerId()));
                }
                commentPageResp.setTargetPlayerName(targetPlayer.getName());
                commentPageResp.setTargetPlayerLevel(targetPlayer.getLevel());
            }

            commentPageRespList.add(commentPageResp);

        }

        commentPageRespPage.setPages(commentPage.getPages());
        commentPageRespPage.setTotal(commentPage.getTotal());
        commentPageRespPage.setSize(commentPage.getSize());
        commentPageRespPage.setCurrent(commentPage.getCurrent());
        commentPageRespPage.setRecords(commentPageRespList);
        return R.ok(commentPageRespPage);
    }

    private String getTitle(Comment comment,
                            Map<Long, News> newsMap,
                            Map<Long, SoutheastAsia> southeastAsiaMap,
                            Map<Long, Topic> topicMap,
                            Map<Long, Society> societyMap,
                            Map<Long, Politics> politicsMap) {
        if (comment.getInfoType() == InfoEnum.NEWS) {
            if (CollectionUtils.isNotEmpty(newsMap)) {
                News news = newsMap.get(comment.getNewsId());
                if (news != null) {
                    return news.getTitle();
                }
            }
        }
        if (comment.getInfoType() == InfoEnum.SOUTHEAST_ASIA) {
            if (CollectionUtils.isNotEmpty(southeastAsiaMap)) {
                SoutheastAsia news = southeastAsiaMap.get(comment.getNewsId());
                if (news != null) {
                    return news.getTitle();
                }
            }
        }
        if (comment.getInfoType() == InfoEnum.SOCIETY) {
            if (CollectionUtils.isNotEmpty(societyMap)) {
                Society news = societyMap.get(comment.getNewsId());
                if (news != null) {
                    return news.getTitle();
                }
            }
        }
        if (comment.getInfoType() == InfoEnum.TOPIC) {
            if (CollectionUtils.isNotEmpty(topicMap)) {
                Topic news = topicMap.get(comment.getNewsId());
                if (news != null) {
                    return news.getTitle();
                }
            }
        }
        if (comment.getInfoType() == InfoEnum.POLITICS) {
            if (CollectionUtils.isNotEmpty(politicsMap)) {
                Politics news = politicsMap.get(comment.getNewsId());
                if (news != null) {
                    return news.getTitle();
                }
            }
        }

        return null;
    }


    private void newsData(List<Comment> commentList,
                          Map<Long, News> newsMap,
                          Map<Long, SoutheastAsia> southeastAsiaMap,
                          Map<Long, Topic> topicMap,
                          Map<Long, Society> societyMap,
                          Map<Long, Politics> politicsMap) {

        List<Long> newsIdList = new ArrayList<>();
        List<Long> southeastIdList = new ArrayList<>();
        List<Long> topicIdList = new ArrayList<>();
        List<Long> societyIdList = new ArrayList<>();
        List<Long> politicsIdList = new ArrayList<>();

        for (Comment comment : commentList) {
            if (comment.getInfoType() == InfoEnum.NEWS) {
                newsIdList.add(comment.getNewsId());
            }
            if (comment.getInfoType() == InfoEnum.TOPIC) {
                topicIdList.add(comment.getNewsId());
            }
            if (comment.getInfoType() == InfoEnum.SOCIETY) {
                societyIdList.add(comment.getNewsId());
            }
            if (comment.getInfoType() == InfoEnum.POLITICS) {
                politicsIdList.add(comment.getNewsId());
            }
            if (comment.getInfoType() == InfoEnum.SOUTHEAST_ASIA) {
                southeastIdList.add(comment.getNewsId());
            }
        }

        if (CollectionUtils.isNotEmpty(newsIdList)) {
            Map<Long, News> map =newsService.mapByIds(newsIdList);
            if(CollectionUtils.isNotEmpty(map)){
                newsMap.putAll(map);
            }
        }
        if (CollectionUtils.isNotEmpty(southeastIdList)) {
            Map<Long, SoutheastAsia> map = southeastAsiaService.mapByIds(southeastIdList);
            if(CollectionUtils.isNotEmpty(map)){
                southeastAsiaMap.putAll(map);
            }
        }
        if (CollectionUtils.isNotEmpty(topicIdList)) {
            Map<Long, Topic> map = topicService.mapByIds(topicIdList);
            if(CollectionUtils.isNotEmpty(map)){
                topicMap.putAll(map);
            }
        }
        if (CollectionUtils.isNotEmpty(societyIdList)) {
            Map<Long, Society> map = societyService.mapByIds(societyIdList);
            if(CollectionUtils.isNotEmpty(map)){
                societyMap.putAll(map);
            }
        }
        if (CollectionUtils.isNotEmpty(politicsIdList)) {
            Map<Long, Politics> map = politicsService.mapByIds(politicsIdList);
            politicsMap.putAll(map);
        }

    }


}
