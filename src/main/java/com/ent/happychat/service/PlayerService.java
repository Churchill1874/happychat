package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Player;
import com.ent.happychat.pojo.req.player.PlayerPage;

import java.util.List;

/**
 * 用户
 */
public interface PlayerService extends IService<Player> {

    /**
     * 添加用户
     * @param po
     * @return
     */
    boolean add(Player po);

    /**
     * 修改用户状态
     * @return
     */
    boolean updateStatus(Player po);

    /**
     * 修改用户
     * @param po
     * @return
     */
    boolean update(Player po);

    /**
     * 删除用户
     * @param idList
     * @return
     */
    boolean del(List<Long> idList);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    Player getUser(Long id);

    /**
     * 根据账号查询用户
     * @param account
     * @return
     */
    Player findByAccount(String account);

    /**
     * 根据网名查找用户
     * @param name
     * @return
     */
    Player findByName(String name);

    /**
     * 分页查询用户信息
     * @param po
     * @return
     */
    IPage<Player> page(PlayerPage po);

    /**
     * 查询用户列表
     * @param po
     * @return
     */
    List<Player> getList(Player po);


    /**
     * 根据id集合获取用户
     * @param idList
     * @return
     */
    List<Player> findByIds(List<Long> idList);

    /**
     * 根据手机号查找用户
     * @param phoneNumber
     */
    Player findByPhoneNumber(String phoneNumber);

}
