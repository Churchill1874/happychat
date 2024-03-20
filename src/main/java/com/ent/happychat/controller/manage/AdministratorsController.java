package com.ent.happychat.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.pojo.req.PageReq;
import com.ent.happychat.service.AdministratorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "管理员")
@RequestMapping("/manage/administrators")
public class AdministratorsController {

    @Autowired
    private AdministratorsService administratorsService;

    @PostMapping("/list")
    @ApiOperation(value = "分页查询管理员", notes = "分页查询管理员")
    public R<IPage<Administrators>> login(@RequestBody PageReq req) {
        log.info("管理员分页查询入参:{}", JSONObject.toJSONString(req));

        IPage<Administrators> iPage = new Page<>(req.getPageNum(), req.getPageSize());
        QueryWrapper<Administrators> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Administrators::getCreateTime);

        iPage = administratorsService.page(iPage, queryWrapper);
        return R.ok(iPage);
    }

}
