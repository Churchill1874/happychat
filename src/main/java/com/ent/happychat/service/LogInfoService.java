package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.LogInfo;
import com.ent.happychat.pojo.req.login.LogInfoPage;

public interface LogInfoService extends IService<LogInfo> {

    void asyncSave(LogInfo logInfo);

    IPage<LogInfo> queryPage(LogInfoPage req);

}
