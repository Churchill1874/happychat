package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.pojo.resp.privatechat.PrivateChatListResp;
import com.ent.happychat.pojo.resp.privatechat.PrivateChatOuterResp;
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
    public R<IPage<PrivateChatResp>> playerPrivateChatPage(@RequestBody PlayerPrivateChatPageReq req) {
        if (StringUtils.isBlank(req.getAccountA())) {
            return R.failed("未指定对方玩家");
        }
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        req.setAccountB(playerTokenResp.getAccount());

        IPage<PrivateChat> privateChatPage = privateChatService.playerPrivateChatPage(req);
        //分页聊天记录待转换数据
        IPage<PrivateChatResp> privateChatRespPage = new Page<>(privateChatPage.getCurrent(), privateChatPage.getSize());
        privateChatRespPage.setPages(privateChatPage.getPages());
        privateChatRespPage.setTotal(privateChatPage.getTotal());
        privateChatRespPage.setRecords(new ArrayList<>());


        if (CollectionUtils.isEmpty(privateChatPage.getRecords())) {
            return R.ok(privateChatRespPage);
        }

        for(PrivateChat privateChat: privateChatPage.getRecords()){
            PrivateChatResp privateChatResp = BeanUtil.toBean(privateChat, PrivateChatResp.class);
            privateChatResp.setIsSender(privateChat.getSendAccount().equals(playerTokenResp.getAccount()));
            privateChatRespPage.getRecords().add(privateChatResp);
        }

        privateChatService.cleanNotRead(req.getAccountA(),req.getAccountB());
        return R.ok(privateChatRespPage);
    }

    @PostMapping("/privateChatList")
    @ApiOperation(value = "查询自己的外层私信记录", notes = "查询自己的外层私信记录")
    public R<PrivateChatListResp> privateChatPage() {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(true);
        String myAccount = playerTokenResp.getAccount();

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
        List<PrivateChatOuterResp> resultList = new ArrayList<>();

        for(String key: linkedHashMap.keySet()){
            PrivateChat privateChat = linkedHashMap.get(key);
            PrivateChatOuterResp privateChatOuterResp = BeanUtil.toBean(privateChat, PrivateChatOuterResp.class);

            if (myAccount.equals(privateChatOuterResp.getReceiveAccount()) && notReadAccountSet.contains(privateChatOuterResp.getSendAccount())){
                privateChatOuterResp.setNotRead(true);
            }

            PlayerInfo sendPlayer = map.get(privateChatOuterResp.getSendAccount());
            privateChatOuterResp.setSendName(sendPlayer.getName());
            privateChatOuterResp.setSendLevel(sendPlayer.getLevel());
            privateChatOuterResp.setSendAvatarPath(sendPlayer.getAvatarPath());

            PlayerInfo receivePlayer = map.get(privateChatOuterResp.getReceiveAccount());
            privateChatOuterResp.setReceiveName(receivePlayer.getName());
            privateChatOuterResp.setReceiveLevel(receivePlayer.getLevel());
            privateChatOuterResp.setReceiveAvatarPath(receivePlayer.getAvatarPath());

            resultList.add(privateChatOuterResp);
        }

        PrivateChatListResp privateChatListResp = new PrivateChatListResp();
        privateChatListResp.setList(resultList);
        privateChatListResp.setLoginAccount(playerTokenResp.getAccount());
        privateChatListResp.setLoginAvatar(playerTokenResp.getAvatarPath());
        privateChatListResp.setLoginLevel(playerTokenResp.getLevel());
        privateChatListResp.setLoginName(playerTokenResp.getName());

        return R.ok(privateChatListResp);
    }





}
