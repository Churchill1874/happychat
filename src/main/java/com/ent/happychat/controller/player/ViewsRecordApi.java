package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.req.views.ViewsAddReq;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.pojo.resp.views.ViewsRecordPageResp;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.ViewsRecordService;
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
@Api(tags = "浏览")
@RequestMapping("/player/views")
public class ViewsRecordApi {

    @Autowired
    private ViewsRecordService viewsRecordService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询自己浏览记录", notes = "分页查询自己浏览记录")
    public R<IPage<ViewsRecordPageResp>> page(@RequestBody @Valid ViewsRecordPageReq req) {
        IPage<ViewsRecordPageResp> viewsRecordPage = new Page<>();

        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        req.setPlayerId(playerTokenResp.getId());
        IPage<ViewsRecord> iPage = viewsRecordService.queryPage(req);

        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(viewsRecordPage);
        }

        List<ViewsRecordPageResp> viewsRecordPageRespList = new ArrayList<>();

        iPage.getRecords().forEach(viewsRecord -> {
            ViewsRecordPageResp viewsRecordPageResp = BeanUtil.toBean(viewsRecord, ViewsRecordPageResp.class);
            viewsRecordPageResp.setPlayerName(playerTokenResp.getName());
            viewsRecordPageResp.setAccount(playerTokenResp.getAccount());
            viewsRecordPageRespList.add(viewsRecordPageResp);
        });

        viewsRecordPage.setRecords(viewsRecordPageRespList);
        return R.ok(viewsRecordPage);
    }

}
