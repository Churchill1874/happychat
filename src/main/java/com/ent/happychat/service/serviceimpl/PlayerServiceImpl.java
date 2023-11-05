package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.UserStatusEnum;
import com.ent.happychat.entity.Player;
import com.ent.happychat.mapper.PlayerMapper;
import com.ent.happychat.pojo.req.user.UserPageReq;
import com.ent.happychat.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Player po) {
        po.setCreateTime(LocalDateTime.now());
        po.setStatus(UserStatusEnum.NORMAL);
        return this.save(po);
    }

    @Override
    @Transactional
    public boolean updateStatus(Player po) {
        UpdateWrapper<Player> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", po.getId());
        updateWrapper.set("status", po.getStatus());
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Player po) {
        return this.update(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean del(List<Long> idList) {
        return removeByIds(idList);
    }

    @Override
    public Player getUser(Long id) {
        return this.getById(id);
    }

    @Override
    public Player findByAccount(String account) {
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return this.getOne(queryWrapper);
    }

    @Override
    public Player findByName(String name) {
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return this.getOne(queryWrapper);
    }

    @Override
    public IPage<Player> page(UserPageReq po) {
        IPage<Player> iPage = new Page<>(po.getPageNum(), po.getPageSize());
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
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
    public List<Player> getList(Player po) {
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        //网名
        queryWrapper.eq(StringUtils.isNotBlank(po.getName()), "name", po.getName());
        //账号
        queryWrapper.eq(po.getAccount() != null, "account", po.getAccount());
        //手机号
        queryWrapper.eq(StringUtils.isNotBlank(po.getPhone()), "phone_number", po.getPhone());
        //真实姓名
        queryWrapper.eq(StringUtils.isNotBlank(po.getRealName()), "real_name", po.getRealName());
        //等级
        queryWrapper.eq(po.getLevel() != null, "level", po.getLevel());
        //地址
        queryWrapper.like(StringUtils.isNotBlank(po.getAddress()), "address", po.getAddress());
        //状态
        queryWrapper.eq(po.getStatus() != null, "status", po.getStatus());
        //创建时间
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public List<Player> findByIds(List<Long> idList) {
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public Player findByPhoneNumber(String phoneNumber) {
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", phoneNumber);
        return getOne(queryWrapper);
    }

}
