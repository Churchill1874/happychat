package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.pojo.dto.privatechat.PrivateChatPersonInfoDto;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;
import com.ent.happychat.pojo.resp.privatechat.PrivateChatResp;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PrivateChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@Api(tags = "私聊")
@RequestMapping("/player/privateChat")
public class PrivateChatApi {

    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private PrivateChatService privateChatService;

    @PostMapping("/playerPrivateChatPage")
    @ApiOperation(value = "指定某个玩家与自己的聊天记录查询", notes = "指定某个玩家与自己的聊天记录查询")
    public R<IPage<PrivateChat>> playerPrivateChatPage(@RequestBody PlayerPrivateChatPageReq req) {
        if (StringUtils.isBlank(req.getAccountA())) {
            return R.failed("未指定对方玩家");
        }
        String account = TokenTools.getPlayerToken(true).getAccount();
        req.setAccountB(account);

        IPage<PrivateChat> iPage = privateChatService.playerPrivateChatPage(req);

        if (CollectionUtils.isEmpty(iPage.getRecords())) {
            return R.ok(iPage);
        }

        privateChatService.cleanNotRead(req.getAccountA(),req.getAccountB());
        return R.ok(iPage);
    }

    @PostMapping("/privateChatList")
    @ApiOperation(value = "查询自己的外层私信记录", notes = "查询自己的外层私信记录")
    public R<List<PrivateChatResp>> privateChatPage() {
        String myAccount = TokenTools.getPlayerToken(true).getAccount();

        List<PrivateChat> list = privateChatService.listByAccount(myAccount);
        if (CollectionUtils.isEmpty(list)){
            return R.ok(null);
        }

        //待查询详细信息的账号
        Set<String> set = new HashSet<>();
        set.add(myAccount);

        //待保留有未读记录的账号
        Set<String> notReadAccountSet = new HashSet<>();

        Map<String, PrivateChat> linkedHashMap = new LinkedHashMap<>();

        for(PrivateChat privateChat: list){
            //不论聊天记录的发送人和接收人是谁,只保留过滤对方的账号最为过滤依据
            String chatTargetAccount = myAccount.equals(privateChat.getSendAccount()) ? privateChat.getReceiveAccount() : privateChat.getSendAccount();

            if (!linkedHashMap.containsKey(chatTargetAccount)){
                set.add(chatTargetAccount);
                linkedHashMap.put(chatTargetAccount, privateChat);
            }

            if (!privateChat.getStatus()){
                notReadAccountSet.add(chatTargetAccount);
            }
        }

        Map<String, PlayerInfo> map = playerInfoService.accountMapPlayer(new ArrayList<>(set));
        List<PrivateChatResp> resultList = new ArrayList<>();

        for(String key: linkedHashMap.keySet()){
            PrivateChat privateChat = linkedHashMap.get(key);
            PrivateChatResp privateChatResp = BeanUtil.toBean(privateChat, PrivateChatResp.class);

            if (myAccount.equals(privateChatResp.getReceiveAccount()) && notReadAccountSet.contains(privateChatResp.getSendAccount())){
                privateChatResp.setNotRead(true);
            }

            PlayerInfo sendPlayer = map.get(privateChatResp.getSendAccount());
            privateChatResp.setSendName(sendPlayer.getName());
            privateChatResp.setSendLevel(sendPlayer.getLevel());
            privateChatResp.setSendAvatarPath(sendPlayer.getAvatarPath());

            PlayerInfo receivePlayer = map.get(privateChatResp.getReceiveAccount());
            privateChatResp.setReceiveName(receivePlayer.getName());
            privateChatResp.setReceiveLevel(receivePlayer.getLevel());
            privateChatResp.setReceiveAvatarPath(receivePlayer.getAvatarPath());

            resultList.add(privateChatResp);
        }

        return R.ok(resultList);
    }





}
