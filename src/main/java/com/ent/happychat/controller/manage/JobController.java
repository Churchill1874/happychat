package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Job;
import com.ent.happychat.pojo.req.Id;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.job.JobAddReq;
import com.ent.happychat.pojo.req.job.JobUpdateReq;
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
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "工作管理")
@RequestMapping("/manage/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @AdminLoginCheck
    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<Job>> queryPage(@RequestBody @Valid PageBase req) {
        return R.ok(jobService.queryPage(req));
    }

    @AdminLoginCheck
    @PostMapping("/updateLastTime")
    @ApiOperation(value = "更新最后招聘需求时间", notes = "更新最后招聘需求时间")
    public R updateLastTime(@RequestBody @Valid Id req) {
        Job job = jobService.getById(req.getId());

        if (job == null) {
            return R.failed("数据不存在");
        }

        job.setLastTime(LocalDateTime.now());
        return R.ok(jobService.updateById(job));
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "新增")
    public R add(@RequestBody @Valid JobAddReq req) {
        Job job = BeanUtil.toBean(req, Job.class);
        job.setCreateName(TokenTools.getAdminToken(true).getName());
        job.setCreateTime(LocalDateTime.now());
        job.setLastTime(LocalDateTime.now());
        return R.ok(jobService.save(job));
    }

    @AdminLoginCheck
    @PostMapping("/update")
    @ApiOperation(value = "更新", notes = "更新")
    public R update(@RequestBody @Valid JobUpdateReq req) {
        Job job = BeanUtil.toBean(req, Job.class);
        return R.ok(jobService.updateById(job));
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid Id req) {
        return R.ok(jobService.removeById(req.getId()));
    }


}
