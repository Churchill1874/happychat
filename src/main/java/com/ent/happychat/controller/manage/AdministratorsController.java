package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.SuperAdminLoginCheck;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.pojo.req.Id;
import com.ent.happychat.pojo.req.Page;
import com.ent.happychat.pojo.req.admin.AdministratorsAdd;
import com.ent.happychat.service.AdministratorsService;
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
@Api(tags = "管理员")
@RequestMapping("/manage/administrators")
public class AdministratorsController {

    @Autowired
    private AdministratorsService administratorsService;

    @PostMapping("/list")
    @ApiOperation(value = "分页查询管理员", notes = "分页查询管理员")
    public R<IPage<Administrators>> list(@RequestBody Page req) {
        log.info("管理员分页查询入参:{}", JSONObject.toJSONString(req));

        IPage<Administrators> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(req.getPageNum(), req.getPageSize());
        QueryWrapper<Administrators> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Administrators::getCreateTime);

        iPage = administratorsService.page(iPage, queryWrapper);
        return R.ok(iPage);
    }


    @PostMapping("/add")
    @ApiOperation(value = "创建管理员", notes = "创建管理员")
    @SuperAdminLoginCheck
    public R add(@RequestBody AdministratorsAdd req) {
        log.info("创建管理员入参:{}", JSONObject.toJSONString(req));
        Administrators administrators = BeanUtil.toBean(req, Administrators.class);
        administrators.setCreateTime(LocalDateTime.now());
        administratorsService.save(administrators);
        return R.ok(null);
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除管理员", notes = "删除管理员")
    @SuperAdminLoginCheck
    public R delete(@RequestBody @Valid Id req) {
        log.info("删除管理员入参:{}", JSONObject.toJSONString(req));
        administratorsService.removeById(req.getId());
        return R.ok(null);
    }


}
