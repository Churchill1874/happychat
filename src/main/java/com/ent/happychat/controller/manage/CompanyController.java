package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Company;
import com.ent.happychat.entity.CompanyEvent;
import com.ent.happychat.pojo.req.Id;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.company.CompanyAddReq;
import com.ent.happychat.pojo.req.company.CompanyUpdateReq;
import com.ent.happychat.pojo.req.company.event.CompanyEventAddReq;
import com.ent.happychat.pojo.req.company.event.CompanyEventUpdateReq;
import com.ent.happychat.pojo.resp.company.CompanyResp;
import com.ent.happychat.service.CompanyEventService;
import com.ent.happychat.service.CompanyService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "公司管理")
@RequestMapping("/manage/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyEventService companyEventService;

    @AdminLoginCheck
    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<CompanyResp>> queryPage(@RequestBody @Valid PageBase req) {
        return R.ok(companyService.queryPage(req));
    }

    @AdminLoginCheck
    @PostMapping("/addCompany")
    @ApiOperation(value = "添加公司", notes = "添加公司")
    public R addCompany(@RequestBody @Valid CompanyAddReq req) {
        Company company = BeanUtil.toBean(req, Company.class);
        company.setCreateTime(LocalDateTime.now());
        company.setCreateName(TokenTools.getAdminToken(true).getName());
        return R.ok(companyService.save(company));
    }

    @AdminLoginCheck
    @PostMapping("/addEvent")
    @ApiOperation(value = "添加事件", notes = "添加事件")
    public R addEvent(@RequestBody @Valid CompanyEventAddReq req) {
        CompanyEvent companyEvent = BeanUtil.toBean(req, CompanyEvent.class);
        companyEvent.setCreateName(TokenTools.getAdminToken(true).getName());
        companyEvent.setCreateTime(LocalDateTime.now());
        return R.ok(companyEventService.save(companyEvent));
    }

    @AdminLoginCheck
    @PostMapping("/updateCompany")
    @ApiOperation(value = "更新公司", notes = "更新公司")
    public R updateCompany(@RequestBody @Valid CompanyUpdateReq req) {
        Company company = BeanUtil.toBean(req, Company.class);
        Company oldData = companyService.getById(req.getId());
        company.setCreateName(oldData.getCreateName());
        company.setCreateTime(oldData.getCreateTime());
        return R.ok(companyService.updateById(company));
    }


    @AdminLoginCheck
    @PostMapping("/updateEvent")
    @ApiOperation(value = "修改事件", notes = "修改事件")
    public R updateEvent(@RequestBody @Valid CompanyEventUpdateReq req) {
        CompanyEvent companyEvent = BeanUtil.toBean(req, CompanyEvent.class);
        CompanyEvent oldData = companyEventService.getById(req.getId());
        companyEvent.setCreateName(oldData.getCreateName());
        companyEvent.setCreateTime(oldData.getCreateTime());
        return R.ok(companyEventService.updateById(companyEvent));
    }


    @AdminLoginCheck
    @PostMapping("/deleteCompany")
    @ApiOperation(value = "删除公司", notes = "删除公司")
    public R delete(@RequestBody @Valid Id req) {
        companyService.removeById(req.getId());

        QueryWrapper<CompanyEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyEvent::getCompanyId, req.getId());

        return R.ok(companyEventService.remove(queryWrapper));
    }


    @AdminLoginCheck
    @PostMapping("/deleteEvent")
    @ApiOperation(value = "删除公司的某个事件记录", notes = "删除公司的某个事件记录")
    public R deleteEvent(@RequestBody @Valid Id req) {
        return R.ok(companyEventService.removeById(req.getId()));
    }



}
