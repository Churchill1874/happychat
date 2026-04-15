package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.LogInfo;
import com.ent.happychat.pojo.req.login.LogInfoPage;
import com.ent.happychat.service.LogInfoService;
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
@Api(tags = "日志记录")
@RequestMapping("/manage/logInfo")
public class LogInfoController {

    @Autowired
    private LogInfoService logInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<LogInfo>> queryPage(@RequestBody @Valid LogInfoPage req) {
        return R.ok(logInfoService.queryPage(req));
    }

}
