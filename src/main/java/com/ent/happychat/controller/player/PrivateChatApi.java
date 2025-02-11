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
@Api(tags = "玩家")
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
        String account = TokenTools.getPlayerToken(true).getAccount();
        req.setAccountB(account);

        IPage<PrivateChat> iPage = privateChatService.playerPrivateChatPage(req);
        IPage<PrivateChatResp> respIPage = new Page<>(req.getPageNum(), req.getPageSize());
        respIPage.setPages(iPage.getPages());
        respIPage.setCurrent(iPage.getCurrent());
        respIPage.setTotal(iPage.getTotal());

        if (CollectionUtils.isEmpty(iPage.getRecords())) {
            return R.ok(respIPage);
        }

        Map<String, PrivateChatPersonInfoDto> map = new HashMap<>();
        //将a账号信息放入集合
        PrivateChatPersonInfoDto privateChatADto = new PrivateChatPersonInfoDto();
        privateChatADto.setAccount(req.getAccountA());
        privateChatADto.setName(req.getAccountAName());
        privateChatADto.setAvatarPath(req.getAccountAAvatarPath());
        privateChatADto.setLevel(req.getAccountALevel());
        map.put(req.getAccountA(), privateChatADto);
        //将b用户信息放入结合
        PrivateChatPersonInfoDto privateChatBDto = new PrivateChatPersonInfoDto();
        privateChatBDto.setAccount(req.getAccountB());
        privateChatBDto.setName(req.getAccountBName());
        privateChatBDto.setAvatarPath(req.getAccountBAvatarPath());
        privateChatBDto.setLevel(req.getAccountBLevel());
        map.put(req.getAccountB(), privateChatBDto);

        List<PrivateChatResp> list = new ArrayList<>();
        for (PrivateChat privateChat : iPage.getRecords()) {
            PrivateChatResp privateChatResp = BeanUtil.toBean(privateChat, PrivateChatResp.class);
            //拼装账号信息
            PrivateChatPersonInfoDto sendDto = map.get(privateChatResp.getSendAccount());
            privateChatResp.setSendName(sendDto.getName());
            privateChatResp.setSendLevel(sendDto.getLevel());
            privateChatResp.setSendAvatarPath(sendDto.getAvatarPath());

            PrivateChatPersonInfoDto receiveDto = map.get(privateChatResp.getReceiveAccount());
            privateChatResp.setReceiveName(receiveDto.getName());
            privateChatResp.setReceiveLevel(receiveDto.getLevel());
            privateChatResp.setReceiveAvatarPath(receiveDto.getAvatarPath());
            list.add(privateChatResp);
        }

        respIPage.setRecords(list);
        return R.ok(respIPage);
    }

    @PostMapping("/privateChatPage")
    @ApiOperation(value = "查询自己的私信记录", notes = "查询自己的私信记录")
    public R<List<PrivateChatResp>> privateChatPage() {
        String account = TokenTools.getPlayerToken(true).getAccount();

        List<PrivateChat> list = privateChatService.listByAccount(account);
        if (CollectionUtils.isEmpty(list)){
            return R.ok(null);
        }

        Set<String> set = new HashSet<>();
        Map<String, PrivateChat> linkedHashMap = new LinkedHashMap<>();

        for(PrivateChat privateChat: list){
            //不论聊天记录的发送人和接收人是谁,只保留过滤对方的账号最为过滤依据
            String chatTargetAccount = account.equals(privateChat.getSendAccount()) ? privateChat.getReceiveAccount() : privateChat.getSendAccount();

            if (!linkedHashMap.containsKey(chatTargetAccount)){
                set.add(chatTargetAccount);
                linkedHashMap.put(chatTargetAccount, privateChat);
            }
        }

        set.add(account);
        Map<String, PlayerInfo> map = playerInfoService.accountMapPlayer(new ArrayList<>(set));
        List<PrivateChatResp> resultList = new ArrayList<>();

        for(String key: linkedHashMap.keySet()){
            PrivateChat privateChat = linkedHashMap.get(key);
            PrivateChatResp privateChatResp = BeanUtil.toBean(privateChat, PrivateChatResp.class);

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
