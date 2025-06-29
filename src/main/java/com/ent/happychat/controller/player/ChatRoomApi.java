package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.entity.ChatRoom;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.PageAndIdReq;
import com.ent.happychat.pojo.resp.chatroom.ChatRoomResp;
import com.ent.happychat.service.ChatRoomService;
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
@Api(tags = "聊天室")
@RequestMapping("/player/chatRoom")
public class ChatRoomApi {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询聊天室", notes = "分页查询聊天室")
    public R<IPage<ChatRoomResp>> queryPage(@RequestBody @Valid PageAndIdReq req) {
        IPage<ChatRoom> iPage = chatRoomService.queryPage(req);
        IPage<ChatRoomResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        page.setPages(iPage.getPages());
        page.setTotal(iPage.getTotal());
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(page);
        }

        List<Long> playerIdList = iPage.getRecords().stream().map(ChatRoom::getPlayerId).collect(Collectors.toList());
        Map<Long, PlayerInfo> map = playerInfoService.mapByIds(playerIdList);

        List<ChatRoomResp> list = new ArrayList<>();
        for(ChatRoom chatRoom: iPage.getRecords()){
            ChatRoomResp chatRoomResp = BeanUtil.toBean(chatRoom, ChatRoomResp.class);
            PlayerInfo playerInfo = map.get(chatRoom.getPlayerId());
            if (playerInfo != null){
                chatRoomResp.setLevel(playerInfo.getLevel());
                chatRoomResp.setAvatarPath(playerInfo.getAvatarPath());
                chatRoomResp.setName(playerInfo.getName());
                list.add(chatRoomResp);
            }
        }

        page.setRecords(list);
        return R.ok(page);
    }

}
