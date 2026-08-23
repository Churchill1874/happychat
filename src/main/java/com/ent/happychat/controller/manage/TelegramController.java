// 目标路径: src/main/java/com/ent/happychat/controller/manage/TelegramController.java
package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.entity.Telegram;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.telegram.TelegramAddReq;
import com.ent.happychat.pojo.req.telegram.TelegramPageReq;
import com.ent.happychat.pojo.req.telegram.TelegramUpdateReq;
import com.ent.happychat.service.TelegramService;
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

@Slf4j
@RestController
@Api(tags = "电报频道/群组管理")
@RequestMapping("/manage/telegram")
public class TelegramController {

    @Resource
    private TelegramService telegramService;
    @Resource
    private UploadRecordService uploadRecordService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    @AdminLoginCheck
    public R<IPage<Telegram>> queryPage(@RequestBody TelegramPageReq req) {
        IPage<Telegram> iPage = telegramService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @ApiOperation(value = "新增电报频道/群组", notes = "新增电报频道/群组")
    public R add(@RequestBody @Valid TelegramAddReq req) {
        Telegram telegram = BeanUtil.toBean(req, Telegram.class);
        telegramService.add(telegram);

        uploadRecordService.cleanByPath(telegram.getPosterImagePath());
        uploadRecordService.cleanByPath(telegram.getQrImagePath());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        Telegram telegram = telegramService.getById(req.getId());
        telegramService.removeById(req.getId());

        if (telegram != null) {
            uploadRecordService.cleanRemoveFile(telegram.getPosterImagePath());
            uploadRecordService.cleanRemoveFile(telegram.getQrImagePath());
        }
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<Telegram> findById(@RequestBody @Valid IdBase req) {
        Telegram telegram = telegramService.getById(req.getId());
        return R.ok(telegram);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "修改")
    @AdminLoginCheck
    public R update(@RequestBody @Valid TelegramUpdateReq req) {
        Telegram telegram = BeanUtil.toBean(req, Telegram.class);
        telegramService.updateById(telegram);

        uploadRecordService.cleanByPath(telegram.getPosterImagePath());
        uploadRecordService.cleanByPath(telegram.getQrImagePath());
        return R.ok(null);
    }

}
