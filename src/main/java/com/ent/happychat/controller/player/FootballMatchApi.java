package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.FootballMatch;
import com.ent.happychat.pojo.Id;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "足球赛事")
@RequestMapping("/player/footballMatch")
public class FootballMatchApi {

    @Autowired
    private FootballMatchService footballMatchService;

    @PostMapping("/getById")
    @ApiOperation(value = "根据id查询赛事详情", notes = "根据id查询赛事详情")
    public R<FootballMatch> updateScore(@RequestBody @Valid Id req) {
        return R.ok(footballMatchService.getById(req.getId()));
    }

    @PostMapping("/getNotStart")
    @ApiOperation(value = "获取未开始的足球赛事(可投注的)", notes = "获取未开始的足球赛事(可投注的)")
    public R<List<FootballMatch>> getNotStart() {
        return R.ok(footballMatchService.getNotStart());
    }

    @PostMapping("/getInProgress")
    @ApiOperation(value = "获取进行中的赛事(用于比分直播)", notes = "获取进行中的赛事(用于比分直播)")
    public R<List<FootballMatch>> getInProgress() {
        return R.ok(footballMatchService.getInProgress());
    }

    @PostMapping("/get3DayFinished")
    @ApiOperation(value = "获取3天内的完赛数据(查看完赛的比分赛局)", notes = "获取3天内的完赛数据(查看完赛的比分赛局)")
    public R<List<FootballMatch>> get3DayFinished() {
        return R.ok(footballMatchService.get3DayFinished());
    }

}
