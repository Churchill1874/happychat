package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.annotation.SuperAdminLoginCheck;
import com.ent.happychat.common.constant.RoleEnum;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.User;
import com.ent.happychat.pojo.IdList;
import com.ent.happychat.pojo.req.user.UserCreateReq;
import com.ent.happychat.pojo.req.user.UserPageReq;
import com.ent.happychat.pojo.req.user.UserStatusUpdateReq;
import com.ent.happychat.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "用户")
@RequestMapping("/manage/user")
public class UserController {

    @Autowired
    private UserService userService;

    @SuperAdminLoginCheck
    @PostMapping("/del")
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    public R<Boolean> del(@RequestBody @Valid IdList req) {
        return R.ok(userService.del(req.getIdList()));
    }

    @AdminLoginCheck
    @PostMapping("/page")
    @ApiOperation(value = "分页查询用户", notes = "分页查询用户")
    public R<IPage<User>> page(@RequestBody @Valid UserPageReq req) {
        Integer platform = TokenTools.getToken().getPlatform();
        if (platform != 0){
            req.setPlatform(platform);
        }
        return R.ok(userService.page(req));
    }

    @SuperAdminLoginCheck
    @PostMapping("/create")
    @ApiOperation(value = "创建管理员", notes = "创建管理员")
    public synchronized R<Boolean> create(@RequestBody @Valid UserCreateReq req) {
        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setAccount(userService.maxAccount() + 1);
        user.setRole(RoleEnum.ADMIN.getCode());
        user.setPassword(CodeTools.md5AndSalt(req.getPassword()));
        user.setAvatar(10);
        user.setPlatform(req.getPlatform());
        return R.ok(userService.add(user));
    }

    @SuperAdminLoginCheck
    @PostMapping("/updateStatus")
    @ApiOperation(value = "修改用户状态", notes = "修改用户状态")
    public R updateStatus(@RequestBody @Valid UserStatusUpdateReq req) {
        User userRecord = userService.getById(req.getId());
        if (userRecord == null) {
            return R.failed("操作的记录id不存在:" + req.getId());
        }

        if (TokenTools.getToken().getAccount().equals(userRecord.getAccount())) {
            return R.failed("不可修改自己的账号状态");
        }

        User user = new User();
        user.setId(req.getId());
        user.setStatus(req.getStatus());
        return R.ok(userService.updateStatus(user));
    }


}
