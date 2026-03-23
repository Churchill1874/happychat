package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.HomeDataClean;
import com.ent.happychat.entity.Society;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.society.SocietyAddReq;
import com.ent.happychat.pojo.req.society.SocietyPageReq;
import com.ent.happychat.pojo.req.society.SocietyUpdateReq;
import com.ent.happychat.service.SocietyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "社会管理")
@RequestMapping("/manage/society")
public class SocietyController {

    @Resource
    private SocietyService societyService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<Society>> queryPage(@RequestBody SocietyPageReq req) {
        IPage<Society> iPage = societyService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @HomeDataClean
    @ApiOperation(value = "新增社会新闻", notes = "新增东南亚新闻")
    public R add(@RequestBody SocietyAddReq req) {
        Society society = BeanUtil.toBean(req, Society.class);
        societyService.add(society);
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @HomeDataClean
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        societyService.removeById(req.getId());
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/findById")
    @HomeDataClean
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<Society> findById(@RequestBody @Valid IdBase req) {
        Society society = societyService.getById(req.getId());
        return R.ok(society);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "修改")
    @HomeDataClean
    public R update(@RequestBody @Valid SocietyUpdateReq req) {
        Society society = BeanUtil.toBean(req, Society.class);
        societyService.updateById(society);
        return R.ok(null);
    }


}
