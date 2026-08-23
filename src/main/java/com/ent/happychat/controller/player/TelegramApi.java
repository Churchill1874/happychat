// 目标路径: src/main/java/com/ent/happychat/controller/player/TelegramApi.java
package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.Telegram;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.telegram.TelegramPageReq;
import com.ent.happychat.service.TelegramService;
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
@Api(tags = "电报频道/群组")
@RequestMapping("/player/telegram")
public class TelegramApi {

    @Autowired
    private TelegramService telegramService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询（列表页）", notes = "分页查询")
    public R<IPage<Telegram>> queryPage(@RequestBody TelegramPageReq req) {
        // 前台列表只展示已上架的记录，不管前端有没有传 status 都强制过滤，
        // 避免后台下架/草稿状态的记录被搜索引擎抓到。
        req.setStatus(true);
        IPage<Telegram> iPage = telegramService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/find")
    @ApiOperation(value = "根据id查询（详情页）", notes = "根据id查询")
    public R<Telegram> find(@RequestBody @Valid IdBase req) {
        Telegram telegram = telegramService.getById(req.getId());
        if (telegram == null || !Boolean.TRUE.equals(telegram.getStatus())) {
            return R.failed("该记录不存在或已下架");
        }
        return R.ok(telegram);
    }

}
