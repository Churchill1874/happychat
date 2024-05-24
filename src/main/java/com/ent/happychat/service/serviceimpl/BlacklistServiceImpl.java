package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Blacklist;
import com.ent.happychat.mapper.BlacklistMapper;
import com.ent.happychat.service.BlacklistService;
import com.ent.happychat.service.EhcacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, Blacklist> implements BlacklistService {

    @Lazy
    @Autowired
    private EhcacheService ehcacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Blacklist po) {
        po.setCreateTime(LocalDateTime.now());
        return save(po);
    }

    @Override
    public List<Blacklist> getList(Blacklist po) {
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(po.getIp()), Blacklist::getIp, po.getIp())
                .eq(StringUtils.isNotBlank(po.getPhone()), Blacklist::getPhone, po.getPhone())
                .eq(StringUtils.isNotBlank(po.getDevice()), Blacklist::getDevice, po.getDevice());
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean del(List<Long> idList) {
        return removeByIds(idList);
    }

    @Override
    //@Cacheable(value = "blacklist",key = "'page'")
    public IPage<Blacklist> queryPage(Integer pageNum, Integer pageSize, String ip, String phone, String device) {
        IPage<Blacklist> iPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(ip), Blacklist::getIp, ip)
                .eq(StringUtils.isNotBlank(phone), Blacklist::getPhone, phone)
                .eq(StringUtils.isNotBlank(device), Blacklist::getDevice, device)
                .orderByDesc(Blacklist::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    //@Cacheable(value = "blacklist", key = "#ip")
    public Blacklist findByIp(String ip) {
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Blacklist::getIp, ip);
        return getOne(queryWrapper);
    }

    @Override
    public Set<String> getIpSet() {
        Set<String> set = ehcacheService.getBlacklistIpSetCache();

        if (CollectionUtils.isNotEmpty(set)){
            return set;
        }

        if (set == null){
            set = new HashSet<>();
        }

        List<Blacklist> list = list();
        if (CollectionUtils.isNotEmpty(list)){
            for(Blacklist blacklist: list){
                set.add(blacklist.getIp());
            }
        }

        //更新黑名单缓存
        ehcacheService.setBlacklistIpSetCache(set);
        return set;
    }

}
