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
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.comment.BotCommentSend;
import com.ent.happychat.pojo.req.topic.TopicAddReq;
import com.ent.happychat.pojo.req.topic.TopicPageReq;
import com.ent.happychat.pojo.req.topic.TopicUpdateReq;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.TopicService;
import com.ent.happychat.service.UploadRecordService;
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
@Api(tags = "话题")
@RequestMapping("/manage/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private UploadRecordService uploadRecordService;
    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private CommentService commentService;


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
            comment.setInfoType(InfoEnum.TOPIC);
            comment.setLikesCount(0);
            comment.setCommentsCount(0);
            comment.setReadStatus(false);
            comment.setCreateTime(LocalDateTime.now().minusSeconds(RandomUtil.randomInt(0, 3600 * 6)));
            comment.setCreateName(playerInfo.getName());
            commentList.add(comment);
        }

        Topic topic = topicService.getById(req.getId());

        if (CollectionUtils.isNotEmpty(commentList) && topic != null) {
            topic.setViewCount(topic.getViewCount() + commentList.size());
            topic.setCommentsCount(topic.getCommentsCount() + commentList.size());
            topicService.updateById(topic);

            commentService.saveBatch(commentList);
        }
        return R.ok(null);
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    @AdminLoginCheck
    public R<IPage<Topic>> queryPage(@RequestBody TopicPageReq req) {
        IPage<Topic> iPage = topicService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @HomeDataClean
    @ApiOperation(value = "新增东南亚新闻", notes = "新增东南亚新闻")
    public R add(@RequestBody TopicAddReq req) {
        topicService.add(req);
        uploadRecordService.cleanByPath(req.getImagePath());
        uploadRecordService.cleanByPath(req.getVideoPath());
        uploadRecordService.cleanByPath(req.getVideoCover());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @HomeDataClean
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        Topic topic = topicService.getById(req.getId());
        topicService.removeById(req.getId());
        uploadRecordService.cleanRemoveFile(topic.getImagePath());
        uploadRecordService.cleanRemoveFile(topic.getVideoPath());
        uploadRecordService.cleanRemoveFile(topic.getVideoCover());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<Topic> findById(@RequestBody @Valid IdBase req) {
        Topic topic = topicService.getById(req.getId());
        return R.ok(topic);
    }

    @PostMapping("/update")
    @HomeDataClean
    @AdminLoginCheck
    @ApiOperation(value = "修改", notes = "修改详情")
    public R update(@RequestBody @Valid TopicUpdateReq req) {
        Topic topic = BeanUtil.toBean(req, Topic.class);
        topicService.update(topic);
        uploadRecordService.cleanByPath(req.getImagePath());
        uploadRecordService.cleanByPath(req.getVideoPath());
        uploadRecordService.cleanByPath(req.getVideoCover());
        return R.ok(null);
    }

}
