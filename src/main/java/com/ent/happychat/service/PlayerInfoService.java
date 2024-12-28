package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.pojo.req.PageBase;

import java.util.List;
import java.util.Map;

public interface PlayerInfoService extends IService<PlayerInfo> {

    IPage<PlayerInfo> queryPage(PlayerInfo playerInfo, PageBase pageBase);

    void add(PlayerInfo playerInfo);

    PlayerInfo findByAccount(String account);

    void updateStatus(Long id , Boolean status);

    PlayerInfo findByLogin(String account, String name, String phone, String email);

    PlayerInfo findByName(String name);

    /**
     * 根据玩家id列表 转化map
     * @param idList
     * @return
     */
    Map<Long, PlayerInfo> mapByIds(List<Long> idList);

    /**
     * 根据玩家id只查询基本字段
     * @param id
     * @return
     */
    PlayerInfo getBaseInfoById(Long id);

    /**
     * 查询账号列表
     * @param isBot
     * @return
     */
    List<PlayerInfo> queryList(boolean isBot);

    /**
     * 转换用户集合
     * @param accountList
     * @return
     */
    Map<String, PlayerInfo> accountMapPlayer(List<String> accountList);

}
