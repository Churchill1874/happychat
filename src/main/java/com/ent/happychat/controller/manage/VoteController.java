package com.ent.happychat.controller.manage;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.Vote;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.comment.BotCommentSend;
import com.ent.happychat.pojo.req.vote.VoteAddReq;
import com.ent.happychat.pojo.req.vote.VotePageReq;
import com.ent.happychat.pojo.req.vote.VoteUpdateReq;
import com.ent.happychat.pojo.resp.vote.VoteResp;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.VoteService;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "投票管理")
@RequestMapping("/manage/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private CommentService commentService;

    @AdminLoginCheck
    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<VoteResp>> queryPage(@RequestBody VotePageReq req) {
        return R.ok(voteService.queryPage(req, false));
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @ApiOperation(value = "查看详情（含选项和票数）", notes = "查看详情")
    public R<VoteResp> findById(@RequestBody @Valid IdBase req) {
        return R.ok(voteService.detail(req.getId(), null));
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @ApiOperation(value = "新增投票", notes = "新增投票")
    public R add(@RequestBody @Valid VoteAddReq req) {
        voteService.add(req);
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/update")
    @ApiOperation(value = "修改投票", notes = "选项带id为修改，不带id为新增，未传入的原有选项会被删除")
    public R update(@RequestBody @Valid VoteUpdateReq req) {
        voteService.update(req);
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除投票、选项和投票记录")
    public R delete(@RequestBody @Valid IdBase req) {
        voteService.delete(req.getId());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/sendBotComments")
    @ApiOperation(value = "发送机器评论", notes = "发送机器评论")
    public R sendBotComments(@RequestBody @Valid BotCommentSend req) {
        if (CollectionUtils.isEmpty(req.getContentList())) {
            return R.failed("评论内容不能为空");
        }
        Vote vote = voteService.getById(req.getId());
        if (vote == null) {
            return R.failed("投票不存在");
        }
        List<PlayerInfo> playerInfoList = playerInfoService.allListBot();
        if (CollectionUtils.isEmpty(playerInfoList)) {
            return R.ok(null);
        }

        List<Comment> commentList = new ArrayList<>();
        for (String content : req.getContentList()) {
            PlayerInfo playerInfo = playerInfoList.get(RandomUtil.randomInt(0, playerInfoList.size()));
            Comment comment = new Comment();
            comment.setNewsId(req.getId());
            comment.setPlayerId(playerInfo.getId());
            comment.setContent(content);
            comment.setInfoType(InfoEnum.VOTE);
            comment.setLikesCount(0);
            comment.setCommentsCount(0);
            comment.setReadStatus(false);
            comment.setCreateTime(LocalDateTime.now().minusSeconds(RandomUtil.randomInt(0, 3600 * 6)));
            comment.setCreateName(playerInfo.getName());
            commentList.add(comment);
        }
        commentService.saveBatch(commentList);

        Vote update = new Vote();
        update.setId(vote.getId());
        update.setCommentsCount((vote.getCommentsCount() == null ? 0 : vote.getCommentsCount()) + commentList.size());
        voteService.updateById(update);
        return R.ok(null);
    }

}
