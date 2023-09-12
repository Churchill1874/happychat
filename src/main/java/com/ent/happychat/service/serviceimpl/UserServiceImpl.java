package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.UserStatusEnum;
import com.ent.happychat.entity.User;
import com.ent.happychat.mapper.UserMapper;
import com.ent.happychat.pojo.req.user.UserPageReq;
import com.ent.happychat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(User po) {
        po.setCreateTime(LocalDateTime.now());
        po.setStatus(UserStatusEnum.NORMAL.getValue());
        return this.save(po);
    }

    @Override
    @Transactional
    public boolean updateStatus(User po) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", po.getId());
        updateWrapper.set("status", po.getStatus());
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(User po) {
        return this.update(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean del(List<Long> idList) {
        return removeByIds(idList);
    }

    @Override
    public User getUser(Long id) {
        return this.getById(id);
    }

    @Override
    public User findByAccount(Integer account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return this.getOne(queryWrapper);
    }

    @Override
    public User findByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return this.getOne(queryWrapper);
    }

    @Override
    public IPage<User> page(UserPageReq po) {
        IPage<User> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //网名
        queryWrapper.like(StringUtils.isNotBlank(po.getName()), "name", po.getName());
        //账号
        queryWrapper.eq(po.getAccount() != null, "account", po.getAccount());
        //手机号
        queryWrapper.eq(StringUtils.isNotBlank(po.getPhoneNumber()), "phone_number", po.getPhoneNumber());
        //真实姓名
        queryWrapper.like(StringUtils.isNotBlank(po.getRealName()), "real_name", po.getRealName());
        //等级
        queryWrapper.eq(po.getLevel() != null, "level", po.getLevel());
        //地址
        queryWrapper.like(StringUtils.isNotBlank(po.getAddress()), "address", po.getAddress());
        //状态
        queryWrapper.eq(po.getStatus() != null, "status", po.getStatus());
        //角色
        queryWrapper.eq(po.getRole() != null, "role", po.getRole());
        //平台
        queryWrapper.eq(po.getPlatform() != null, "platform", po.getPlatform());
        //创建时间
        queryWrapper.orderByDesc("create_time");
        return page(iPage, queryWrapper);
    }

    @Override
    public List<User> getList(User po) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //网名
        queryWrapper.eq(StringUtils.isNotBlank(po.getName()), "name", po.getName());
        //账号
        queryWrapper.eq(po.getAccount() != null, "account", po.getAccount());
        //手机号
        queryWrapper.eq(StringUtils.isNotBlank(po.getPhoneNumber()), "phone_number", po.getPhoneNumber());
        //真实姓名
        queryWrapper.eq(StringUtils.isNotBlank(po.getRealName()), "real_name", po.getRealName());
        //等级
        queryWrapper.eq(po.getLevel() != null, "level", po.getLevel());
        //地址
        queryWrapper.like(StringUtils.isNotBlank(po.getAddress()), "address", po.getAddress());
        //状态
        queryWrapper.eq(po.getStatus() != null, "status", po.getStatus());
        //角色
        queryWrapper.eq(po.getRole() != null, "role", po.getRole());
        //平台
        queryWrapper.eq(po.getPlatform() != null, "platform", po.getPlatform());
        //创建时间
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public List<User> findByIds(List<Long> idList) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public int maxAccount() {
        return userMapper.maxCount();
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", phoneNumber);
        return getOne(queryWrapper);
    }

}
