package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageAndIdReq;
import com.ent.happychat.pojo.req.likes.LikesRecordPageReq;
import com.ent.happychat.pojo.req.news.NewsLikesReq;
import com.ent.happychat.pojo.resp.BooleanResp;
import com.ent.happychat.pojo.resp.likes.LikesRecordPageResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.PlayerInfoService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private PlayerInfoService playerInfoService;


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
    public R<IPage<LikesRecordPageResp>> page(@RequestBody @Valid PageAndIdReq req) {
        LikesRecordPageReq likesRecordPageReq = new LikesRecordPageReq();
        likesRecordPageReq.setTargetPlayerId(req.getId());
        IPage<LikesRecord> likesRecordIPage = likesRecordService.queryPage(likesRecordPageReq);

        IPage<LikesRecordPageResp> iPage = new Page<>(req.getPageNum(), req.getPageSize());
        if (CollectionUtils.isNotEmpty(likesRecordIPage.getRecords())){
            //查点赞的用户们信息
            List<Long> playerIdList = likesRecordIPage.getRecords().stream().map(LikesRecord::getPlayerId).collect(Collectors.toList());
            Map<Long, PlayerInfo> map = playerInfoService.mapByIds(playerIdList);

            //转换返回对象
            List<LikesRecordPageResp> list = new ArrayList<>();
            for(LikesRecord likesRecord: likesRecordIPage.getRecords()){
                LikesRecordPageResp likesRecordPageResp = BeanUtil.toBean(likesRecord, LikesRecordPageResp.class);
                PlayerInfo playerInfo = map.get(likesRecord.getPlayerId());
                likesRecordPageResp.setLevel(playerInfo.getLevel());
                likesRecordPageResp.setAccount(playerInfo.getAccount());
                likesRecordPageResp.setAvatarPath(playerInfo.getAvatarPath());
                likesRecordPageResp.setPlayerName(playerInfo.getName());
                list.add(likesRecordPageResp);
            }

            iPage.setRecords(list);
        }

        return R.ok(iPage);
    }

}
