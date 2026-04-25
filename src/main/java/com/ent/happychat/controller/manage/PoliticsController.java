package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.mapper.PoliticsMapper;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.comment.BotCommentSend;
import com.ent.happychat.pojo.req.politics.PoliticsAdd;
import com.ent.happychat.pojo.req.politics.PoliticsPageReq;
import com.ent.happychat.pojo.req.politics.PoliticsUpdate;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PoliticsService;
import com.ent.happychat.service.UploadRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "政治新闻管理")
@RequestMapping("/manage/politics")
public class PoliticsController {

    @Resource
    private PoliticsService politicsService;
    @Resource
    private UploadRecordService uploadRecordService;
    @Resource
    private CommentService commentService;
    @Resource
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    @AdminLoginCheck
    public R<IPage<Politics>> queryPage(@RequestBody PoliticsPageReq req) {
        IPage<Politics> iPage = politicsService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @HomeDataClean
    @ApiOperation(value = "新增政治新闻", notes = "新增政治新闻")
    public R add(@RequestBody PoliticsAdd req) {
        Politics politics = BeanUtil.toBean(req, Politics.class);
        politics.setCommentsCount(0);
        politics.setLikesCount(0);
        politics.setCreateTime(LocalDateTime.now());
        politics.setCreateName(TokenTools.getAdminName());
        politicsService.save(politics);
        uploadRecordService.cleanByPath(politics.getImagePath());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @HomeDataClean
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        Politics politics = politicsService.getById(req.getId());
        politicsService.removeById(req.getId());
        uploadRecordService.cleanRemoveFile(politics.getImagePath());

        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @HomeDataClean
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<Politics> findById(@RequestBody @Valid IdBase req) {
        Politics politics = politicsService.getById(req.getId());
        return R.ok(politics);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "修改")
    @HomeDataClean
    @AdminLoginCheck
    public R update(@RequestBody @Valid PoliticsUpdate req) {
        Politics politics = BeanUtil.toBean(req, Politics.class);
        politicsService.updateById(politics);
        uploadRecordService.cleanByPath(politics.getImagePath());
        return R.ok(null);
    }


    @AdminLoginCheck
    @PostMapping("/sendBotComments")
    @ApiOperation(value = "发送机器评论", notes = "发送机器评论")
    public R sendBotComments(@RequestBody @Valid BotCommentSend req) {
        if (CollectionUtils.isEmpty(req.getContentList())) {
            return R.failed("评论内容不能为空");
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
            comment.setInfoType(InfoEnum.POLITICS);
            comment.setLikesCount(0);
            comment.setCommentsCount(0);
            comment.setReadStatus(false);
            comment.setCreateTime(LocalDateTime.now().minusSeconds(RandomUtil.randomInt(0, 3600 * 6)));
            comment.setCreateName(playerInfo.getName());
            commentList.add(comment);
        }

        Politics politics = politicsService.getById(req.getId());

        if (CollectionUtils.isNotEmpty(commentList) && politics != null) {
            politics.setViewCount(politics.getViewCount() + commentList.size());
            politics.setCommentsCount(politics.getCommentsCount() + commentList.size());
            politicsService.updateById(politics);

            commentService.saveBatch(commentList);
        }

        return R.ok(null);
    }


}
