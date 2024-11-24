package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.ImageConfig;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.image.ImageConfigAddReq;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;
import com.ent.happychat.pojo.req.image.ImageConfigUpdateBase;
import com.ent.happychat.service.ImageConfigService;
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

@Slf4j
@RestController
@Api(tags = "图片管理")
@RequestMapping("/manage/imageConfig")
public class ImageConfigController {

    @Autowired
    private ImageConfigService imageConfigService;
    @Autowired
    private UploadRecordService uploadRecordService;

    @PostMapping("/add")
    @ApiOperation(value = "图片添加", notes = "图片添加")
    @AdminLoginCheck
    public R add(@RequestBody @Valid ImageConfigAddReq req) {
        ImageConfig imageConfig = BeanUtil.toBean(req, ImageConfig.class);
        imageConfig.setCreateTime(LocalDateTime.now());
        imageConfig.setCreateName(TokenTools.getAdminToken(true).getName());
        imageConfigService.add(imageConfig);
        return R.ok(null);
    }

    @PostMapping("/update")
    @ApiOperation(value = "图片修改", notes = "图片修改")
    @AdminLoginCheck
    public R update(@RequestBody @Valid ImageConfigUpdateBase req) {
        ImageConfig imageConfig = imageConfigService.getById(req.getId());
        imageConfig.setSort(req.getSort());
        imageConfig.setStatus(req.getStatus());
        imageConfig.setUpdateName(TokenTools.getAdminToken(true).getName());
        imageConfig.setUpdateTime(LocalDateTime.now());
        imageConfigService.updateById(imageConfig);
        return R.ok(null);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "图片删除", notes = "图片删除")
    @AdminLoginCheck
    public R delete(@RequestBody @Valid IdBase req) {
        imageConfigService.deleteById(req.getId());
        return R.ok(null);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R page(@RequestBody ImageConfigPageReq req) {
        return R.ok(imageConfigService.queryPage(req));
    }

}
