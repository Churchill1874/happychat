package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.entity.News;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.likes.LikesClickReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "点赞")
@RequestMapping("/player/likes")
public class LikesRecordApi {

    @Autowired
    private LikesRecordService likesRecordService;


}
