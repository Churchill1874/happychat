package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.mapper.AdministratorsMapper;
import com.ent.happychat.service.AdministratorsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdministratorsServiceImpl extends ServiceImpl<AdministratorsMapper, Administrators> implements AdministratorsService {

    @Override
    public Administrators findByAccount(String account) {
        QueryWrapper<Administrators> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Administrators::getAccount, account);
        return getOne(queryWrapper);
    }

}
