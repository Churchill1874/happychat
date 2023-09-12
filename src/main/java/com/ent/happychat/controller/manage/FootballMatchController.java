package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.entity.FootballMatch;
import com.ent.happychat.pojo.Id;
import com.ent.happychat.pojo.IdList;
import com.ent.happychat.pojo.req.footballmatch.FootballMatchAddReq;
import com.ent.happychat.pojo.req.footballmatch.FootballMatchPageReq;
import com.ent.happychat.pojo.req.footballmatch.FootballMatchScoreUpdateReq;
import com.ent.happychat.pojo.req.footballmatch.FootballMatchUpdateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "足球赛事")
@RequestMapping("/manage/footballMatch")
public class FootballMatchController {

    @Autowired
    private FootballMatchService footballMatchService;

    @AdminLoginCheck
    @PostMapping("/getById")
    @ApiOperation(value = "根据id查询赛事详情", notes = "根据id查询赛事详情")
    public R<FootballMatch> updateScore(@RequestBody @Valid Id req) {
        return R.ok(footballMatchService.getById(req.getId()));
    }

    @AdminLoginCheck
    @PostMapping("/page")
    @ApiOperation(value = "分页足球赛事", notes = "分页足球赛事")
    public R<IPage<FootballMatch>> page(@RequestBody FootballMatchPageReq req) {
        return R.ok(footballMatchService.page(req));
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @ApiOperation(value = "添加足球赛事", notes = "添加足球赛事")
    public R add(@RequestBody @Valid FootballMatchAddReq req) {
        if (req.getStartTime().isBefore(LocalDateTime.now())) {
            return R.failed("开始时间设置有误");
        }

        FootballMatch footballMatch = new FootballMatch();
        BeanUtils.copyProperties(req, footballMatch);
        return R.ok(footballMatchService.add(footballMatch));
    }


    @AdminLoginCheck
    @PostMapping("/del")
    @ApiOperation(value = "删除足球赛事", notes = "删除足球赛事")
    public R del(@RequestBody @Valid IdList req) {
        return R.ok(footballMatchService.delByIds(req.getIdList()));
    }

    @AdminLoginCheck
    @PostMapping("/update")
    @ApiOperation(value = "修改足球赛事", notes = "修改足球赛事")
    public R update(@RequestBody @Valid FootballMatchUpdateReq req) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (req.getStartTime().isBefore(currentTime)) {
            return R.failed("开始时间设置有误");
        }

        FootballMatch oldDate = footballMatchService.getById(req.getId());
        if (oldDate == null) {
            return R.failed("未找到该赛事数据");
        }

        if (oldDate.getStartTime().isBefore(currentTime)) {
            return R.failed("不能修改已开始的比赛");
        }

        FootballMatch footballMatch = new FootballMatch();
        BeanUtils.copyProperties(req, footballMatch);
        return R.ok(footballMatchService.updateFootballMatch(footballMatch));
    }

    @AdminLoginCheck
    @PostMapping("/updateScore")
    @ApiOperation(value = "修改分数", notes = "修改分数")
    public R updateScore(@RequestBody FootballMatchScoreUpdateReq req) {
        if (req.getHomeTeamScore() == null && req.getVisitingTeamScore() == null) {
            return R.failed("分数不能为空");
        }
        return R.ok(footballMatchService.updateScore(req.getId(), req.getHomeTeamScore(), req.getVisitingTeamScore()));
    }


}