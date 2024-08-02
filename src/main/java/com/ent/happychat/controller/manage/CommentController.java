package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.News;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.resp.comment.CommentPageResp;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.NewsService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "评论管理")
@RequestMapping("/manage/comment")
public class CommentController {

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
        List<Long> newsIdList = commentPage.getRecords().stream().map(Comment::getNewsId).collect(Collectors.toList());
        Map<Long, News> newsMap = newsService.mapByIds(newsIdList);

        List<Long> playerIdList = commentPage.getRecords().stream().map(Comment::getPlayerId).collect(Collectors.toList());
        List<Long> targetPlayerIdList = commentPage.getRecords().stream().map(Comment::getTargetPlayerId).collect(Collectors.toList());
        playerIdList.addAll(targetPlayerIdList);
        Map<Long, PlayerInfo> playerInfoMap = playerInfoService.mapByIds(playerIdList);


        List<CommentPageResp> commentPageRespList = new ArrayList<>();
        commentPage.getRecords().forEach(comment -> {
            PlayerInfo player = playerInfoMap.get(comment.getPlayerId());
            PlayerInfo targetPlayer = playerInfoMap.get(comment.getTargetPlayerId());
            News news = newsMap.get(comment.getNewsId());

            CommentPageResp commentPageResp = BeanUtil.toBean(comment, CommentPageResp.class);
            commentPageResp.setCommentator(player.getName());
            commentPageResp.setTitle(news.getTitle());
            commentPageResp.setLevel(player.getLevel());
            commentPageResp.setTargetPlayerName(targetPlayer.getName());
            commentPageResp.setTargetPlayerLevel(targetPlayer.getLevel());

            commentPageRespList.add(commentPageResp);
        });

        commentPageRespPage.setRecords(commentPageRespList);
        return R.ok(commentPageRespPage);
    }
}
