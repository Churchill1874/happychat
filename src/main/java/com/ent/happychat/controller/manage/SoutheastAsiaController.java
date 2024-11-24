package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.entity.SoutheastAsia;
import com.ent.happychat.pojo.req.IdBase;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaAddReq;
import com.ent.happychat.pojo.req.southeastasia.SoutheastAsiaPageReq;
import com.ent.happychat.service.SoutheastAsiaService;
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
@Api(tags = "东南亚新闻管理")
@RequestMapping("/manage/southeastAsia")
public class SoutheastAsiaController {

    @Autowired
    private SoutheastAsiaService southeastAsiaService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<SoutheastAsia>> queryPage(@RequestBody SoutheastAsiaPageReq req) {
        IPage<SoutheastAsia> iPage = southeastAsiaService.queryPage(req);
        return R.ok(iPage);
    }

    @AdminLoginCheck
    @PostMapping("/add")
    @ApiOperation(value = "新增东南亚新闻", notes = "新增东南亚新闻")
    public R add(@RequestBody SoutheastAsiaAddReq req) {
        SoutheastAsia southeastAsia = BeanUtil.toBean(req, SoutheastAsia.class);
        southeastAsiaService.add(southeastAsia);
        return R.ok(null);
    }

    @AdminLoginCheck
    @PostMapping("/delete")
    @ApiOperation(value = "删除", notes = "删除")
    public R delete(@RequestBody @Valid IdBase req) {
        southeastAsiaService.removeById(req.getId());
        return R.ok(null);
    }

    @PostMapping("/findById")
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public R<SoutheastAsia> findById(@RequestBody @Valid IdBase req) {
        SoutheastAsia southeastAsia = southeastAsiaService.getById(req.getId());
        return R.ok(southeastAsia);
    }



}
