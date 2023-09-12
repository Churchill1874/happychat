package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.LogRecord;
import com.ent.happychat.pojo.req.log.LogPageReq;
import com.ent.happychat.service.LogRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "日志")
@RequestMapping("/manage/logRecord")
public class LogRecordController {

    @Autowired
    private LogRecordService logRecordService;

    @AdminLoginCheck
    @PostMapping("/page")
    @ApiOperation(value = "分页日志", notes = "分页日志")
    public R<IPage<LogRecord>> page(@RequestBody @Valid LogPageReq req) {
        Integer platform = HttpTools.getPlatform();
        if (HttpTools.getPlatform() != 0){
            req.setPlatform(platform);
        }
        return R.ok(logRecordService.page(req));
    }

}
