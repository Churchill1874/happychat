package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.exposure.ExposureAdd;
import com.ent.happychat.pojo.req.exposure.ExposurePage;
import com.ent.happychat.pojo.req.exposure.ExposureUpdate;
import com.ent.happychat.pojo.req.topic.TopicAddReq;
import com.ent.happychat.pojo.req.topic.TopicPageReq;
import com.ent.happychat.pojo.req.topic.TopicUpdateReq;
import com.ent.happychat.service.ExposureService;
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
@RequestMapping("/manage/exposure")
public class ExposureController {

    @Autowired
    private ExposureService exposureService;
    @Autowired
    private UploadRecordService uploadRecordService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    @AdminLoginCheck
    public R<IPage<Exposure>> queryPage(@RequestBody ExposurePage req) {
        IPage<Exposure> iPage = exposureService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @HomeDataClean
    @ApiOperation(value = "新增东南亚新闻", notes = "新增东南亚新闻")
    public R add(@RequestBody ExposureAdd req) {
        Exposure exposure = BeanUtil.toBean(req, Exposure.class);
        exposureService.add(exposure);

        uploadRecordService.cleanByPath(exposure.getImage1());
        uploadRecordService.cleanByPath(exposure.getImage2());
        uploadRecordService.cleanByPath(exposure.getImage3());
        uploadRecordService.cleanByPath(exposure.getImage4());
        uploadRecordService.cleanByPath(exposure.getImage5());
        uploadRecordService.cleanByPath(exposure.getImage6());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @HomeDataClean
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        Exposure exposure = exposureService.getById(req.getId());
        exposureService.removeById(req.getId());

        uploadRecordService.cleanRemoveFile(exposure.getImage1());
        uploadRecordService.cleanRemoveFile(exposure.getImage2());
        uploadRecordService.cleanRemoveFile(exposure.getImage3());
        uploadRecordService.cleanRemoveFile(exposure.getImage4());
        uploadRecordService.cleanRemoveFile(exposure.getImage5());
        uploadRecordService.cleanRemoveFile(exposure.getImage6());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<Exposure> findById(@RequestBody @Valid IdBase req) {
        Exposure exposure = exposureService.getById(req.getId());
        return R.ok(exposure);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "修改详情")
    @HomeDataClean
    @AdminLoginCheck
    public R update(@RequestBody @Valid ExposureUpdate req) {
        Exposure exposure = BeanUtil.toBean(req, Exposure.class);
        exposureService.update(exposure);

        uploadRecordService.cleanByPath(exposure.getImage1());
        uploadRecordService.cleanByPath(exposure.getImage2());
        uploadRecordService.cleanByPath(exposure.getImage3());
        uploadRecordService.cleanByPath(exposure.getImage4());
        uploadRecordService.cleanByPath(exposure.getImage5());
        uploadRecordService.cleanByPath(exposure.getImage6());
        return R.ok(null);
    }




}
