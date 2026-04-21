package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Politics;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.politics.PoliticsAdd;
import com.ent.happychat.pojo.req.politics.PoliticsPageReq;
import com.ent.happychat.pojo.req.politics.PoliticsUpdate;
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

@Slf4j
@RestController
@Api(tags = "政治新闻管理")
@RequestMapping("/manage/politics")
public class PoliticsController {

    @Resource
    private PoliticsService politicsService;
    @Resource
    private UploadRecordService uploadRecordService;

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


}
