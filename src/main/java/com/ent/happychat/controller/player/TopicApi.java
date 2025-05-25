package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Society;
import com.ent.happychat.entity.Topic;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.req.topic.TopicPageReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.SocietyService;
import com.ent.happychat.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "话题")
@RequestMapping("/player/topic")
public class TopicApi {

    @Autowired
    private TopicService topicService;


    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<Topic>> queryPage(@RequestBody TopicPageReq req) {
        IPage<Topic> iPage = topicService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/find")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    public R<Topic> find(@RequestBody @Valid IdBase req) {
        PlayerTokenResp playerTokenResp = TokenTools.getPlayerToken(false);
        Long playerId = null;
        String playerName = null;
        if (playerTokenResp != null){
            playerId = playerTokenResp.getId();
            playerName = playerTokenResp.getName();
        }
        Topic topic = topicService.findByIdAndInsertRecord(HttpTools.getIp(), req.getId() ,playerId, playerName);
        return R.ok(topic);
    }


}
