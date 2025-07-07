package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.SystemMessage;
import com.ent.happychat.pojo.req.systemmessage.SystemMessagePageReq;
import com.ent.happychat.pojo.req.systemmessage.SystemMessageReadAllReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.pojo.resp.systemmessage.SystemMessageResp;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.SystemMessageService;
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
@Api(tags = "消息")
@RequestMapping("/player/message")
public class SystemMessageApi {

    @Autowired
    private SystemMessageService systemMessageService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<SystemMessageResp>> queryPage(@RequestBody SystemMessagePageReq req) {
        Long playerId = TokenTools.getPlayerToken(true).getId();
        req.setRecipientId(playerId);

        IPage<SystemMessageResp> systemMessageRespIPage = new Page<>(req.getPageNum(), req.getPageSize());

        IPage<SystemMessage> iPage = systemMessageService.queryPage(req);
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(systemMessageRespIPage);
        }

        List<Long> accountIdList = iPage.getRecords().stream().map(SystemMessage::getSenderId).collect(Collectors.toList());
        Map<Long, PlayerInfo> map = playerInfoService.playerIdMapPlayer(accountIdList);

        List<SystemMessageResp> list = new ArrayList<>();
        for(SystemMessage systemMessage: iPage.getRecords()){
            SystemMessageResp systemMessageResp = BeanUtil.toBean(systemMessage, SystemMessageResp.class);
            PlayerInfo playerInfo = map.get(systemMessageResp.getSenderId());
            systemMessageResp.setAvatar(playerInfo.getAvatarPath());
            systemMessageResp.setSenderName(playerInfo.getName());
            list.add(systemMessageResp);
        }

        systemMessageRespIPage.setRecords(list);
        return R.ok(systemMessageRespIPage);
    }

    @PostMapping("/readAll")
    @ApiOperation(value = "修改已读全部", notes = "修改已读全部")
    public R readAll(@RequestBody @Valid SystemMessageReadAllReq req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        systemMessageService.readAll(playerTokenResp.getId(), req.getType());
        return R.ok(null);
    }

}
