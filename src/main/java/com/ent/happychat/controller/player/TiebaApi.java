package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Tieba;
import com.ent.happychat.entity.TiebaComment;
import com.ent.happychat.mapper.TiebaCommentMapper;
import com.ent.happychat.mapper.TiebaMapper;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.tieba.TiebaAdd;
import com.ent.happychat.pojo.req.tieba.TiebaPage;
import com.ent.happychat.pojo.req.tieba.comment.TiebaCommentAdd;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.TiebaCommentService;
import com.ent.happychat.service.TiebaService;
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

@Slf4j
@RestController
@Api(tags = "贴吧")
@RequestMapping("/player/tieba")
public class TiebaApi {

    @Autowired
    private TiebaService tiebaService;
    @Autowired
    private TiebaMapper tiebaMapper;
    @Autowired
    private TiebaCommentMapper tiebaCommentMapper;
    @Autowired
    private TiebaCommentService tiebaCommentService;

    @PostMapping("/send")
    @ApiOperation(value = "发帖子", notes = "发帖子")
    public R send(@RequestBody @Valid TiebaAdd req) {
        PlayerTokenResp player = TokenTools.getPlayerToken(true);

        Tieba tieba = BeanUtil.toBean(req, Tieba.class);
        tieba.setAvatar(player.getAvatarPath());
        tieba.setPlayerId(player.getId());
        tieba.setAccount(player.getAccount());
        tieba.setAddress(HttpTools.getAddress());
        tieba.setLevel(player.getLevel());
        tieba.setIsTop(false);
        tieba.setIsHot(false);
        tieba.setCreateTime(LocalDateTime.now());
        tieba.setCreateName(player.getName());
        tiebaService.send(tieba);

        return R.ok(null);
    }

    @PostMapping("/sendComment")
    @ApiOperation(value = "发送评论", notes = "发送评论")
    public R sendComment(@RequestBody @Valid TiebaCommentAdd req) {
        PlayerTokenResp player = TokenTools.getPlayerToken(true);
        TiebaComment tiebaComment = BeanUtil.toBean(req, TiebaComment.class);
        tiebaComment.setAvatar(player.getAvatarPath());
        tiebaComment.setAccount(player.getAccount());
        tiebaComment.setPlayerId(player.getId());
        tiebaComment.setAddress(player.getAddress());
        tiebaComment.setLevel(player.getLevel());
        tiebaComment.setCreateTime(LocalDateTime.now());
        tiebaComment.setCreateName(player.getName());

        if (tiebaComment.getReplyId() != null) {
            TiebaComment replyComment = tiebaCommentService.getById(tiebaComment.getReplyId());
            if (replyComment != null) {
                if (replyComment.getContent().length() > 20) {
                    tiebaComment.setReplyContent(replyComment.getContent().substring(0, 20) + "...");
                } else {
                    tiebaComment.setReplyContent(replyComment.getContent());
                }
            }
        }

        tiebaCommentService.send(tiebaComment);
        return R.ok(null);
    }

    @PostMapping("/findById")
    @ApiOperation(value = "帖子详情", notes = "帖子详情")
    public R<Tieba> findById(@RequestBody @Valid IdBase req) {
        Tieba tieba = tiebaService.findById(req.getId());
        return R.ok(tieba);
    }

    @PostMapping("/likes")
    @ApiOperation(value = "点赞帖子", notes = "点赞帖子")
    public R likes(@RequestBody @Valid IdBase req) {
        tiebaMapper.increaseLikesCount(req.getId());
        return R.ok(null);
    }

    @PostMapping("/commentLikes")
    @ApiOperation(value = "点赞评论", notes = "点赞评论")
    public R commentLikes(@RequestBody @Valid IdBase req) {
        tiebaCommentMapper.increaseLikesCount(req.getId());
        return R.ok(null);
    }

    @PostMapping("/page")
    @ApiOperation(value = "获取玩家贴吧发帖记录", notes = "获取玩家贴吧发帖记录")
    public R<IPage<Tieba>> page(@RequestBody @Valid TiebaPage req) {
        return R.ok(tiebaService.queryPage(req, null, req.getTitle()));
    }

    @PostMapping("/playerPage")
    @ApiOperation(value = "获取玩家贴吧发帖记录", notes = "获取玩家贴吧发帖记录")
    public R<IPage<Tieba>> playerPage(@RequestBody @Valid TiebaPage req) {
        Long playerId = TokenTools.getPlayerToken(true).getId();
        return R.ok(tiebaService.queryPage(req, playerId, req.getTitle()));
    }


    @PostMapping("/playerCommentPage")
    @ApiOperation(value = "获取玩家贴吧评论记录", notes = "获取玩家贴吧评论记录")
    public R<IPage<TiebaComment>> playerCommentPage(@RequestBody @Valid PageBase req) {
        Long playerId = TokenTools.getPlayerToken(true).getId();
        IPage<TiebaComment> iPage = tiebaCommentService.queryPage(req, playerId, null);
        return R.ok(iPage);
    }


    @PostMapping("/tiebaCommentPage")
    @ApiOperation(value = "获取贴吧评论记录", notes = "获取贴吧评论记录")
    public R<IPage<TiebaComment>> tiebaCommentPage(@RequestBody TiebaPage req) {
        IPage<TiebaComment> iPage = tiebaCommentService.queryPage(req, null, req.getId());
        return R.ok(iPage);
    }


}
