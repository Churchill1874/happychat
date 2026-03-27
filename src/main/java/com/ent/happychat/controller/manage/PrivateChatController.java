package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PrivateChat;
import com.ent.happychat.entity.Society;
import com.ent.happychat.pojo.req.privatechat.PlayerPrivateChatPageReq;
import com.ent.happychat.pojo.req.privatechat.PrivateChatPageReq;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.resp.privatechat.PrivateChatResp;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PrivateChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@Api(tags = "私信聊天")
@RequestMapping("/manage/privateChat")
public class PrivateChatController {

    @Autowired
    private PrivateChatService privateChatService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<PrivateChatResp>> queryPage(@RequestBody PlayerPrivateChatPageReq req) {
        IPage<PrivateChat> iPage = privateChatService.adminPrivateChatPage(req);

        IPage<PrivateChatResp> privateChatIPage = new Page<>(req.getPageNum(), req.getPageSize());
        privateChatIPage.setPages(iPage.getPages());
        privateChatIPage.setTotal(iPage.getTotal());

        if(CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(privateChatIPage);
        }

        Set<Long> playerIdSet = new HashSet<>();
        for(PrivateChat privateChat: iPage.getRecords()){
            if(privateChat.getSendId() != null){
                playerIdSet.add(privateChat.getSendId());
            }
            if(privateChat.getReceiveId() != null){
                playerIdSet.add(privateChat.getReceiveId());
            }
        }

        Map<Long, PlayerInfo> map = playerInfoService.playerIdMapPlayer(new ArrayList<>(playerIdSet));

        List<PrivateChatResp> list = new ArrayList<>();
        for(PrivateChat privateChat: iPage.getRecords()){
            PrivateChatResp privateChatResp = BeanUtil.toBean(privateChat, PrivateChatResp.class);

            if(privateChat.getSendId() != null){
                PlayerInfo playerSender = map.get(privateChat.getSendId());
                if(playerSender != null){
                    privateChatResp.setSenderName(playerSender.getName());
                    privateChatResp.setSendAvatarPath(playerSender.getAvatarPath());
                }
            }

            if(privateChat.getReceiveId() != null){
                PlayerInfo receivePlayer = map.get(privateChat.getReceiveId());
                if(receivePlayer != null){
                    privateChatResp.setReceiverName(receivePlayer.getName());
                    privateChatResp.setReceiveAvatarPath(receivePlayer.getAvatarPath());
                }
            }

            list.add(privateChatResp);
        }

        privateChatIPage.setRecords(list);
        return R.ok(privateChatIPage);
    }

}
