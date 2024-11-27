package com.ent.happychat.controller.manage;
import java.time.LocalDateTime;
import com.ent.happychat.common.constant.enums.InfoEnum;
import com.ent.happychat.common.constant.enums.MessageTypeEnum;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.SystemMessage;
import com.ent.happychat.pojo.req.systemmessage.SystemMessageAddReq;
import com.ent.happychat.pojo.req.systemmessage.SystemMessagePageReq;
import com.ent.happychat.service.SystemMessageService;
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
@Api(tags = "消息管理")
@RequestMapping("/manage/systemMessage")
public class SystemMessageController {

    @Autowired
    private SystemMessageService systemMessageService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<SystemMessage>> queryPage(@RequestBody SystemMessagePageReq req) {
        IPage<SystemMessage> iPage = systemMessageService.queryPage(req);
        return R.ok(iPage);
    }

    @PostMapping("/send")
    @ApiOperation(value = "发送系统消息", notes = "发送系统消息")
    public R send(@RequestBody @Valid SystemMessageAddReq req) {
        systemMessageService.sendSystemMessage(req);
        return R.ok(null);
    }


}
