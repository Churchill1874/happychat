package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Company;
import com.ent.happychat.entity.CompanyEvent;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.req.company.CompanyAddReq;
import com.ent.happychat.pojo.req.company.CompanyUpdateReq;
import com.ent.happychat.pojo.req.company.event.CompanyEventAddReq;
import com.ent.happychat.pojo.req.company.event.CompanyEventUpdateReq;
import com.ent.happychat.pojo.resp.company.CompanyResp;
import com.ent.happychat.service.CompanyEventService;
import com.ent.happychat.service.CompanyService;
import com.ent.happychat.service.UploadRecordService;
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
import java.util.List;

@Slf4j
@RestController
@Api(tags = "公司管理")
@RequestMapping("/manage/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyEventService companyEventService;
    @Autowired
    private UploadRecordService uploadRecordService;

    @AdminLoginCheck
    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<IPage<CompanyResp>> queryPage(@RequestBody @Valid PageBase req) {
        return R.ok(companyService.queryPageCompanyAndEvent(req));
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @ApiOperation(value = "根据id查询公司", notes = "根据id查询公司")
    @HomeDataClean
    public R<CompanyResp> addCompany(@RequestBody @Valid IdBase req) {
        Company company  = companyService.getById(req.getId());
        CompanyResp companyResp = BeanUtil.toBean(company, CompanyResp.class);

        LambdaQueryWrapper<CompanyEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(CompanyEvent::getCompanyId, req.getId())
                .orderByDesc(CompanyEvent::getEventDate);
        List<CompanyEvent> list = companyEventService.list(queryWrapper);
        companyResp.setCompanyEventList(list);

        return R.ok(companyResp);
    }

    @AdminLoginCheck
    @PostMapping("/addCompany")
    @ApiOperation(value = "添加公司", notes = "添加公司")
    @HomeDataClean
    public R addCompany(@RequestBody @Valid CompanyAddReq req) {
        Company company = BeanUtil.toBean(req, Company.class);
        company.setCreateTime(LocalDateTime.now());
        company.setCreateName(TokenTools.getAdminToken(true).getName());

        companyService.save(company);

        uploadRecordService.cleanByPath(company.getImage());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/updateCompany")
    @ApiOperation(value = "更新公司", notes = "更新公司")
    @HomeDataClean
    public R updateCompany(@RequestBody @Valid CompanyUpdateReq req) {
        Company company = BeanUtil.toBean(req, Company.class);
        Company oldData = companyService.getById(req.getId());
        company.setCreateName(oldData.getCreateName());
        company.setCreateTime(oldData.getCreateTime());
        companyService.updateById(company);
        uploadRecordService.cleanByPath(company.getImage());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/deleteCompany")
    @ApiOperation(value = "删除公司", notes = "删除公司")
    @HomeDataClean
    public R delete(@RequestBody @Valid IdBase req) {
        companyService.removeById(req.getId());

        Company company = companyService.getById(req.getId());
        uploadRecordService.cleanRemoveFile(company.getImage());

        QueryWrapper<CompanyEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyEvent::getCompanyId, req.getId());
        companyEventService.remove(queryWrapper);


        return R.ok(null);
    }


    @AdminLoginCheck
    @PostMapping("/addEvent")
    @ApiOperation(value = "添加事件", notes = "添加事件")
    @HomeDataClean
    public R addEvent(@RequestBody @Valid CompanyEventAddReq req) {
        CompanyEvent companyEvent = BeanUtil.toBean(req, CompanyEvent.class);
        companyEvent.setCreateName(TokenTools.getAdminToken(true).getName());
        companyEvent.setCreateTime(LocalDateTime.now());
        return R.ok(companyEventService.save(companyEvent));
    }

    @AdminLoginCheck
    @PostMapping("/updateEvent")
    @HomeDataClean
    @ApiOperation(value = "修改事件", notes = "修改事件")
    public R updateEvent(@RequestBody @Valid CompanyEventUpdateReq req) {
        CompanyEvent companyEvent = BeanUtil.toBean(req, CompanyEvent.class);
        CompanyEvent oldData = companyEventService.getById(req.getId());
        companyEvent.setCreateName(oldData.getCreateName());
        companyEvent.setCreateTime(oldData.getCreateTime());
        return R.ok(companyEventService.updateById(companyEvent));
    }


    @AdminLoginCheck
    @PostMapping("/deleteEvent")
    @HomeDataClean
    @ApiOperation(value = "删除公司的某个事件记录", notes = "删除公司的某个事件记录")
    public R deleteEvent(@RequestBody @Valid IdBase req) {
        return R.ok(companyEventService.removeById(req.getId()));
    }



}
