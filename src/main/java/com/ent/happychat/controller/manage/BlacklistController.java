package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.SuperAdminLoginCheck;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.pojo.IdList;
import com.ent.happychat.pojo.req.blacklist.BlacklistAddReq;
import com.ent.happychat.pojo.req.blacklist.BlacklistPageReq;
import com.ent.happychat.service.BlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "黑名单")
@RequestMapping("/manage/blacklist")
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;

    @AdminLoginCheck
    @PostMapping("/add")
    @ApiOperation(value = "添加黑名单", notes = "添加黑名单")
    public R add(@RequestBody @Valid BlacklistAddReq req) {
        if (StringUtils.isBlank(req.getIp()) && StringUtils.isBlank(req.getPhoneNumber())) {
            return R.failed("ip和手机号不能同时为空");
        }

        //校验ip是否已经添加过
        if (StringUtils.isNotBlank(req.getIp())) {
            List<Blacklist> list = blacklistService.findByIp(req.getIp());
            if (CollectionUtils.isNotEmpty(list)) {
                return R.failed("ip已经添加过");
            }
        }

        Blacklist blacklist = new Blacklist();
        BeanUtils.copyProperties(req, blacklist);

        return R.ok(blacklistService.insert(blacklist));
    }


    @SuperAdminLoginCheck
    @PostMapping("/del")
    @ApiOperation(value = "删除黑名单", notes = "删除黑名单")
    public R del(@RequestBody @Valid IdList req) {
        return R.ok(blacklistService.del(req.getIdList()));
    }


    @AdminLoginCheck
    @PostMapping("/page")
    @ApiOperation(value = "分页黑名单", notes = "分页黑名单")
    public R page(@RequestBody BlacklistPageReq req) {
        return R.ok(blacklistService.page(req.getPageNum(), req.getPageSize(), req.getIp(), req.getPhoneNumber()));
    }


}
