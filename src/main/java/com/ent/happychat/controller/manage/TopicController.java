package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.topic.TopicAddReq;
import com.ent.happychat.pojo.req.topic.TopicPageReq;
import com.ent.happychat.pojo.req.topic.TopicUpdateReq;
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

@Slf4j
@RestController
@Api(tags = "话题")
@RequestMapping("/manage/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private UploadRecordService uploadRecordService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
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
        topicService.removeById(req.getId());
        Topic topic = topicService.getById(req.getId());
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
