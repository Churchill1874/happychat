package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.User;
import com.ent.happychat.pojo.req.user.UserPageReq;

import java.util.List;

/**
 * 用户
 */
public interface UserService extends IService<User> {

    /**
     * 添加用户
     * @param po
     * @return
     */
    boolean add(User po);

    /**
     * 修改用户状态
     * @return
     */
    boolean updateStatus(User po);

    /**
     * 修改用户
     * @param po
     * @return
     */
    boolean update(User po);

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
    User getUser(Long id);

    /**
     * 根据账号查询用户
     * @param account
     * @return
     */
    User findByAccount(Integer account);

    /**
     * 根据网名查找用户
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 分页查询用户信息
     * @param po
     * @return
     */
    IPage<User> page(UserPageReq po);

    /**
     * 查询用户列表
     * @param po
     * @return
     */
    List<User> getList(User po);


    /**
     * 根据id集合获取用户
     * @param idList
     * @return
     */
    List<User> findByIds(List<Long> idList);

    /**
     * 获取最大账号值
     * @return
     */
    int maxAccount();

    /**
     * 根据手机号查找用户
     * @param phoneNumber
     */
    User findByPhoneNumber(String phoneNumber);

}
