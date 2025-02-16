package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.ImageConfig;
import com.ent.happychat.pojo.req.image.ImageConfigPageReq;
import com.ent.happychat.service.ImageConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "图片")
@RequestMapping("/player/imageConfig")
public class ImageConfigApi {

    @Autowired
    private ImageConfigService imageConfigService;

    @PostMapping("/list")
    @ApiOperation(value = "图片查询", notes = "图片查询")
    public R<List<ImageConfig>> page(@RequestBody ImageConfigPageReq req) {
        QueryWrapper<ImageConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(ImageConfig::getStatus,true)
            .eq(ImageConfig::getImageType,req.getImageType())
            .orderByDesc(ImageConfig::getSort).orderByDesc(ImageConfig::getCreateTime);

        return R.ok(imageConfigService.list(queryWrapper));
    }


}
