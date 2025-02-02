package com.ent.happychat.controller.manage;

import com.ent.happychat.service.PrivateChatService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "私信聊天")
@RequestMapping("/manage/privateChat")
public class PrivateChatController {

    @Autowired
    private PrivateChatService privateChatService;



}
