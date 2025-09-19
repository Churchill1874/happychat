package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.Exposure;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.comment.CommentPageReq;
import com.ent.happychat.pojo.resp.comment.NewsCommentPageResp;
import com.ent.happychat.service.ExposureService;
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
@Api(tags = "曝光")
@RequestMapping("/player/exposure")
public class ExposureApi {

    @Autowired
    private ExposureService exposureService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页曝光信息", notes = "分页曝光信息")
    public R<IPage<Exposure>> queryPage(@RequestBody @Valid PageBase req) {
        return R.ok(exposureService.queryPage(req));
    }

    @PostMapping("/findById")
    @ApiOperation(value = "查详情", notes = "查详情")
    public R<Exposure> findById(@RequestBody @Valid IdBase req) {
        return R.ok(exposureService.getById(req.getId()));
    }



}
