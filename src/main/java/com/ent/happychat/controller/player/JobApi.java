package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.entity.Job;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.service.JobService;
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
@Api(tags = "岗位")
@RequestMapping("/player/job")
public class JobApi {

    @Autowired
    private JobService jobService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<Job>> queryPage(@RequestBody @Valid PageBase req) {
        return R.ok(jobService.queryPage(req));
    }

}
