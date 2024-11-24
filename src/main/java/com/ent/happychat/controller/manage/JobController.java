package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Job;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.job.JobAddReq;
import com.ent.happychat.pojo.req.job.JobUpdateReq;
import com.ent.happychat.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public R updateLastTime(@RequestBody @Valid IdBase req) {
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
        checkLength(job);
        job.setCreateName(TokenTools.getAdminToken(true).getName());
        job.setCreateTime(LocalDateTime.now());
        job.setLastTime(LocalDateTime.now());
        return R.ok(jobService.save(job));
    }

    private void checkLength(Job req){
        if (StringUtils.isNotBlank(req.getWelfare()) && req.getWelfare().length() > 100){
            throw new DataException("其他福利长度1-100");
        }
        if (StringUtils.isNotBlank(req.getRoomOut()) && req.getRoomOut().length() > 20){
            throw new DataException("外宿补贴长度1-20");
        }
        if (StringUtils.isNotBlank(req.getEducationConditions()) && req.getEducationConditions().length() > 20){
            throw new DataException("学历长度1-20");
        }
        if (StringUtils.isNotBlank(req.getAgeConditions()) && req.getAgeConditions().length() > 20){
            throw new DataException("年龄范围长度1-20");
        }
        if (StringUtils.isNotBlank(req.getTag()) && req.getTag().length() > 30){
            throw new DataException("标签长度1-30");
        }
        if (StringUtils.isNotBlank(req.getAnnualLeave()) && req.getAnnualLeave().length() > 30){
            throw new DataException("年假长度1-30");
        }
    }

    @AdminLoginCheck
    @PostMapping("/update")
    @ApiOperation(value = "更新", notes = "更新")
    public R update(@RequestBody @Valid JobUpdateReq req) {
        Job job = BeanUtil.toBean(req, Job.class);
        checkLength(job);
        job.setLastTime(LocalDateTime.now());
        return R.ok(jobService.updateById(job));
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        return R.ok(jobService.removeById(req.getId()));
    }

}
