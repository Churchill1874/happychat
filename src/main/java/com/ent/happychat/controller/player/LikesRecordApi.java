package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.likes.LikesRecordPageReq;
import com.ent.happychat.pojo.req.news.NewsLikesReq;
import com.ent.happychat.pojo.resp.BooleanResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.PoliticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "点赞")
@RequestMapping("/player/likes")
public class LikesRecordApi {

    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private PoliticsService politicsService;


    @PostMapping("/increaseLikesCount")
    @ApiOperation(value = "点赞新闻", notes = "点赞新闻")
    public R<BooleanResp> increaseLikesCount(@RequestBody @Valid NewsLikesReq req) {
        //插入点赞记录
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);

        if (req.getInfoType() == InfoEnum.NEWS){
            newsService.increaseLikesCount(req.getId(), playerTokenResp);

        }
        if (req.getInfoType() == InfoEnum.POLITICS){
            politicsService.increaseLikesCount(req.getId(), playerTokenResp);
        }

        BooleanResp booleanResp = new BooleanResp();
        booleanResp.setValue(true);
        return R.ok(booleanResp);
    }

    @PostMapping("/page")
    @ApiOperation(value = "被点赞记录分页查询", notes = "被点赞记录分页查询")
    public R<IPage<LikesRecord>> page(@RequestBody @Valid IdBase req) {
        LikesRecordPageReq likesRecordPageReq = new LikesRecordPageReq();
        likesRecordPageReq.setTargetPlayerId(req.getId());
        IPage<LikesRecord> iPage = likesRecordService.queryPage(likesRecordPageReq);
        return R.ok(iPage);
    }

}
