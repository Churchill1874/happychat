package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.entity.Society;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.comment.BotCommentSend;
import com.ent.happychat.pojo.req.society.SocietyAddReq;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.req.society.SocietyUpdateReq;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.SocietyService;
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
@Api(tags = "社会管理")
@RequestMapping("/manage/society")
public class SocietyController {

    @Resource
    private CommentService commentService;
    @Resource
    private PlayerInfoService playerInfoService;
    @Resource
    private SocietyService societyService;
    @Resource
    private UploadRecordService uploadRecordService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    @AdminLoginCheck
    public R<IPage<Society>> queryPage(@RequestBody SocietyPageReq req) {
        IPage<Society> iPage = societyService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @HomeDataClean
    @ApiOperation(value = "新增社会新闻", notes = "新增东南亚新闻")
    public R add(@RequestBody SocietyAddReq req) {
        Society society = BeanUtil.toBean(req, Society.class);
        societyService.add(society);

        uploadRecordService.cleanByPath(society.getImagePath());
        uploadRecordService.cleanByPath(society.getVideoPath());
        uploadRecordService.cleanByPath(society.getVideoCover());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @HomeDataClean
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        Society society = societyService.getById(req.getId());
        societyService.removeById(req.getId());

        uploadRecordService.cleanRemoveFile(society.getImagePath());
        uploadRecordService.cleanRemoveFile(society.getVideoCover());
        uploadRecordService.cleanRemoveFile(society.getVideoPath());

        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @HomeDataClean
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<Society> findById(@RequestBody @Valid IdBase req) {
        Society society = societyService.getById(req.getId());
        return R.ok(society);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "修改")
    @HomeDataClean
    @AdminLoginCheck
    public R update(@RequestBody @Valid SocietyUpdateReq req) {
        Society society = BeanUtil.toBean(req, Society.class);
        societyService.updateById(society);

        uploadRecordService.cleanByPath(society.getImagePath());
        uploadRecordService.cleanByPath(society.getVideoPath());
        uploadRecordService.cleanByPath(society.getVideoCover());
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
            comment.setInfoType(InfoEnum.SOCIETY);
            comment.setLikesCount(0);
            comment.setCommentsCount(0);
            comment.setReadStatus(false);
            comment.setCreateTime(LocalDateTime.now().minusSeconds(RandomUtil.randomInt(0, 3600 * 6)));
            comment.setCreateName(playerInfo.getName());
            commentList.add(comment);
        }

        Society society = societyService.getById(req.getId());

        if (CollectionUtils.isNotEmpty(commentList) && society != null) {
            society.setViewCount(society.getViewCount() + commentList.size());
            society.setCommentsCount(society.getCommentsCount() + commentList.size());
            societyService.updateById(society);

            commentService.saveBatch(commentList);
        }

        return R.ok(null);
    }


}
