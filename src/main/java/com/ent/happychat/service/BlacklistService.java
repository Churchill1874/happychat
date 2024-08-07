package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Blacklist;

import java.util.List;
import java.util.Set;

/**
 * 黑名单服务
 */
public interface BlacklistService extends IService<Blacklist> {

    /**
     * 添加黑名单
     * @param po
     * @return
     */
    boolean insert(Blacklist po);

    /**
     * 查询黑名单
     * @param po
     * @return
     */
    List<Blacklist> getList(Blacklist po);

    /**
     * 删除黑名单记录
     * @param idList
     * @return
     */
    boolean del(List<Long> idList);

    /**
     * 分页查询黑名单
     * @param pageNum
     * @param pageSize
     * @param ip
     * @param phone
     * @return
     */
    IPage<Blacklist> queryPage(Integer pageNum,Integer pageSize,String ip,String phone,String device);

    /**
     * 通过ip获取黑名单记录
     * @param ip
     * @return
     */
    Blacklist findByIp(String ip);

    /**
     * 获取所有ip集合set
     * @return
     */
    Set<String> getIpSet();
}
