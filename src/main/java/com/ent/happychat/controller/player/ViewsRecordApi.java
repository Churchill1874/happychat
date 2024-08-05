package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.req.views.ViewsAddReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
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

@Slf4j
@RestController
@Api(tags = "浏览")
@RequestMapping("/player/views")
public class ViewsRecordApi {

    @Autowired
    private ViewsRecordService viewsRecordService;

    @PostMapping("/add")
    @ApiOperation(value = "添加浏览记录", notes = "添加浏览记录")
    public R add(@RequestBody @Valid ViewsAddReq req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        ViewsRecord viewsRecord = new ViewsRecord();
        viewsRecord.setPlayerId(playerTokenResp.getId());
        viewsRecord.setViewsId(req.getViewsId());
        viewsRecord.setViewsType(req.getViewsType());
        viewsRecord.setCreateTime(LocalDateTime.now());
        viewsRecord.setCreateName(playerTokenResp.getName());
        viewsRecord.setContent(req.getContent());
        viewsRecordService.save(viewsRecord);
        return R.ok(null);
    }

}
