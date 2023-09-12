package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.LogRecord;
import com.ent.happychat.pojo.req.log.LogPageReq;

import java.util.List;

/**
 * 日志服务
 */
public interface LogRecordService extends IService<LogRecord> {

    /**
     * 插入日志
     * @param po
     */
    void insert(LogRecord po);

    /**
     * 获取5000日志信息
     * @param po
     * @return
     */
    List<LogRecord> getList(LogRecord po);

    /**
     * 分页查询日志
     * @param po
     * @return
     */
    IPage<LogRecord> page(LogPageReq po);

    /**
     * 注册记录查询
     * @param ip
     * @return
     */
    List<LogRecord> registerList(String ip);

}
