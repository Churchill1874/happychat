package com.ent.happychat.controller.player;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.*;
import com.ent.happychat.pojo.req.PageAndIdReq;
import com.ent.happychat.pojo.req.chatroom.ChatRoomSendReq;
import com.ent.happychat.pojo.resp.chatroom.ChatRoomResp;
import com.ent.happychat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.Top;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
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
    @Autowired
    private SoutheastAsiaService southeastAsiaService;
    @Autowired
    private SocietyService societyService;
    @Autowired
    private PoliticsService politicsService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private EhcacheService ehcacheService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询聊天室", notes = "分页查询聊天室")
    public R<IPage<ChatRoomResp>> queryPage(@RequestBody @Valid PageAndIdReq req) {
        IPage<ChatRoom> iPage = chatRoomService.queryPage(req);
        IPage<ChatRoomResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        page.setPages(iPage.getPages());
        page.setTotal(iPage.getTotal());
        if (CollectionUtils.isEmpty(iPage.getRecords())) {
            return R.ok(page);
        }

        List<Long> playerIdList = iPage.getRecords().stream().map(ChatRoom::getPlayerId).collect(Collectors.toList());
        Map<Long, PlayerInfo> map = playerInfoService.mapByIds(playerIdList);

        List<ChatRoomResp> list = new ArrayList<>();
        for (ChatRoom chatRoom : iPage.getRecords()) {
            ChatRoomResp chatRoomResp = BeanUtil.toBean(chatRoom, ChatRoomResp.class);
            PlayerInfo playerInfo = map.get(chatRoom.getPlayerId());
            if (playerInfo != null) {
                chatRoomResp.setLevel(playerInfo.getLevel());
                chatRoomResp.setAvatarPath(playerInfo.getAvatarPath());
                chatRoomResp.setName(playerInfo.getName());
                list.add(chatRoomResp);
            }
        }

        page.setRecords(list);
        return R.ok(page);
    }

    @PostMapping("/send")
    @ApiOperation("发送聊天室信息")
    public R sendRoomMessage(@RequestBody ChatRoomSendReq req) {
        if (StringUtils.isBlank(req.getContent()) || req.getRoomNumber() == null || req.getType() == null) {
            throw new DataException("信息不全");
        }
        ChatRoom chatRoom = BeanUtil.toBean(req, ChatRoom.class);
        chatRoom.setPlayerId(TokenTools.getPlayerToken(true).getId());
        chatRoomService.send(chatRoom);
        return R.ok(null);
    }

    @PostMapping("/scrollingText")
    @ApiOperation("跑马灯")
    public R<List<String>> scrollingText() {
        List<String> list = ehcacheService.scrollingTextCache();
        if(CollectionUtils.isNotEmpty(list)){
            return R.ok(list);
        }

        list = new ArrayList<>();

        LambdaQueryWrapper<SoutheastAsia> southeastAsiaLambdaQueryWrapper = new LambdaQueryWrapper<>();
        southeastAsiaLambdaQueryWrapper
                .select(SoutheastAsia::getTitle)
                .eq(SoutheastAsia::getStatus, true)
                .orderByDesc(SoutheastAsia::getCreateTime)
                .last("limit 10");
        List<SoutheastAsia> southeastAsiasList = southeastAsiaService.list(southeastAsiaLambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(southeastAsiasList)){
            list.addAll(southeastAsiasList.stream().map(SoutheastAsia::getTitle).collect(Collectors.toList()));
        }

        LambdaQueryWrapper<Politics> politicsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        politicsLambdaQueryWrapper
                .select(Politics::getTitle)
                .orderByDesc(Politics::getCreateTime)
                .last("limit 10");
        List<Politics> politics = politicsService.list(politicsLambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(politics)){
            list.addAll(politics.stream().map(Politics::getTitle).collect(Collectors.toList()));
        }

        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper
                .select(Topic::getTitle)
                .orderByDesc(Topic::getCreateTime)
                .last("limit 10");
        List<Topic> topicList = topicService.list(topicLambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(topicList)){
            list.addAll(topicList.stream().map(Topic::getTitle).collect(Collectors.toList()));
        }

        LambdaQueryWrapper<Society> socityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        socityLambdaQueryWrapper
                .select(Society::getTitle)
                .orderByDesc(Society::getCreateTime)
                .last("limit 10");
        List<Society> societyList = societyService.list(socityLambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(societyList)){
            list.addAll(societyList.stream().map(Society::getTitle).collect(Collectors.toList()));
        }

        LambdaQueryWrapper<News> newsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        newsLambdaQueryWrapper
                .select(News::getTitle)
                .orderByDesc(News::getCreateTime)
                .last("limit 10");
        List<News> newsList = newsService.list(newsLambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(newsList)){
            list.addAll(newsList.stream().map(News::getTitle).collect(Collectors.toList()));
        }

        ehcacheService.setScrollingTextCache(list);

        return R.ok(list);
    }


}
