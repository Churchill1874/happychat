package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.likes.LikesRecordPageReq;
import com.ent.happychat.pojo.req.player.PlayerInfoPageReq;
import com.ent.happychat.pojo.resp.likes.LikesRecordPageResp;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.PlayerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "点赞记录")
@RequestMapping("/manage/likes")
public class LikesRecordController {

    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<LikesRecordPageResp>> queryPage(@RequestBody @Valid LikesRecordPageReq req) {
        IPage<LikesRecordPageResp> likesRecordPageRespPage = new Page<>(req.getPageNum(), req.getPageSize());


        IPage<LikesRecord> iPage = likesRecordService.queryPage(req);
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(likesRecordPageRespPage);
        }

        //获取用户信息
        List<Long> playerIdList = iPage.getRecords().stream().map(LikesRecord::getPlayerId).collect(Collectors.toList());
        Map<Long, PlayerInfo> playerInfoMap = playerInfoService.mapByIds(playerIdList);

        List<LikesRecordPageResp> likesRecordPageRespList = new ArrayList<>();
        iPage.getRecords().forEach(likesRecord -> {
            PlayerInfo playerInfo = playerInfoMap.get(likesRecord.getPlayerId());
            LikesRecordPageResp likesRecordPageResp = BeanUtil.toBean(likesRecord, LikesRecordPageResp.class);
            likesRecordPageResp.setPlayerName(playerInfo.getName());
            likesRecordPageResp.setLevel(playerInfo.getLevel());
            likesRecordPageResp.setAccount(playerInfo.getAccount());
            likesRecordPageRespList.add(likesRecordPageResp);
        });

        likesRecordPageRespPage.setRecords(likesRecordPageRespList);
        return R.ok(likesRecordPageRespPage);
    }

}
