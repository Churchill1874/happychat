package com.ent.happychat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Administrators;

public interface AdministratorsService extends IService<Administrators> {

    /**
     * 根据账号查找管理员
     * @param account
     * @return
     */
    Administrators findByAccount(String account);
}
