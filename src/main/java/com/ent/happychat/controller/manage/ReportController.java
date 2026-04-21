package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.pojo.resp.report.CommentReportResp;
import com.ent.happychat.pojo.resp.report.RankReportResp;
import com.ent.happychat.pojo.resp.report.RegisterReportResp;
import com.ent.happychat.pojo.resp.report.ViewReportResp;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.ViewsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "报表")
@RequestMapping("/manage/report")
public class ReportController {
    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ViewsRecordService viewsRecordService;

    @PostMapping("/registerReport")
    @ApiOperation(value = "统计注册", notes = "统计注册信息")
    @AdminLoginCheck
    public R<RegisterReportResp> registerReport() {
        return R.ok(playerInfoService.getRegisterReport());
    }

    @PostMapping("/commentReport")
    @ApiOperation(value = "统计注册", notes = "统计注册信息")
    @AdminLoginCheck
    public R<CommentReportResp> commentReport() {
        return R.ok(commentService.getCommentReport());
    }

    @PostMapping("/viewReport")
    @ApiOperation(value = "统计注册", notes = "统计注册信息")
    @AdminLoginCheck
    public R<ViewReportResp> viewReport() {
        return R.ok(viewsRecordService.viewReport());
    }


    @PostMapping("/rankReport")
    @ApiOperation(value = "统计注册", notes = "统计注册信息")
    @AdminLoginCheck
    public R<RankReportResp> rankReport() {
        return R.ok(playerInfoService.rankReport());
    }




}
