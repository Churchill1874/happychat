package com.ent.happychat.controller.player;

import com.ent.happychat.service.LikesRecordService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "点赞")
@RequestMapping("/player/likes")
public class LikesRecordApi {

    @Autowired
    private LikesRecordService likesRecordService;



}
