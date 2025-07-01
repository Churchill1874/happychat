package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.InteractiveStatistics;
import com.ent.happychat.mapper.PlayerInfoMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.player.PlayerInfoResp;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerInfoServiceImpl extends ServiceImpl<PlayerInfoMapper, PlayerInfo> implements PlayerInfoService {
    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private InteractiveStatisticsService interactiveStatisticsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SystemMessageService systemMessageService;
    @Autowired
    private PrivateChatService privateChatService;


    @Override
    public IPage<PlayerInfo> queryPage(PlayerInfo playerInfo, PageBase pageBase) {
        IPage<PlayerInfo> iPage = new Page<>(pageBase.getPageNum(), pageBase.getPageSize());
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(playerInfo.getName()), PlayerInfo::getName, playerInfo.getName())
                .eq(playerInfo.getAccount() != null, PlayerInfo::getAccount, playerInfo.getAccount())
                .eq(StringUtils.isNotBlank(playerInfo.getPhone()), PlayerInfo::getPhone, playerInfo.getPhone())
                .eq(StringUtils.isNotBlank(playerInfo.getEmail()), PlayerInfo::getEmail, playerInfo.getEmail())
                .eq(StringUtils.isNotBlank(playerInfo.getCity()), PlayerInfo::getCity, playerInfo.getCity())
                .eq(playerInfo.getGender() != null, PlayerInfo::getGender, playerInfo.getGender())
                .eq(playerInfo.getBirth() != null, PlayerInfo::getBirth, playerInfo.getBirth())
                .eq(playerInfo.getLevel() != null, PlayerInfo::getLevel, playerInfo.getLevel())
                .eq(playerInfo.getIsBot() != null, PlayerInfo::getIsBot, playerInfo.getIsBot())
                .eq(playerInfo.getStatus() != null, PlayerInfo::getStatus, playerInfo.getStatus())
                .eq(pageBase.getStartTime() != null, PlayerInfo::getCreateTime, pageBase.getStartTime())
                .eq(pageBase.getEndTime() != null, PlayerInfo::getCreateTime, pageBase.getEndTime())
                .orderByDesc(PlayerInfo::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PlayerInfo playerInfo) {
        PlayerInfo queryInfo = findByAccount(playerInfo.getAccount());
        if (queryInfo != null){
            throw new DataException("该账号已存在");
        }

        save(playerInfo);

        InteractiveStatistics interactiveStatistics = interactiveStatisticsService.findByPlayerId(queryInfo.getId());
        if (interactiveStatistics != null){
            throw new DataException("刚刚网络繁忙,请您在此请求");
        }

        //初始化用户交互信息
        interactiveStatisticsService.init(queryInfo.getId());
    }

    @Override
    public PlayerInfo findByAccount(String account) {
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PlayerInfo::getAccount, account);
        return getOne(queryWrapper);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        UpdateWrapper<PlayerInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(PlayerInfo::getStatus, status)
                .set(PlayerInfo::getUpdateTime, LocalDateTime.now())
                .eq(PlayerInfo::getId, id);
        update(updateWrapper);
    }

    @Override
    public PlayerInfo findByLogin(String account, String name, String phone, String email) {
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(phone), PlayerInfo::getPhone, phone)
                .eq(StringUtils.isNotBlank(email), PlayerInfo::getEmail, email)
                .eq(StringUtils.isNotBlank(name), PlayerInfo::getName, name)
                .eq(account != null, PlayerInfo::getAccount, account);
        return getOne(queryWrapper);
    }

    @Override
    public PlayerInfo findByName(String name) {
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PlayerInfo::getName, name);
        return getOne(queryWrapper);
    }

    @Override
    public Map<Long, PlayerInfo> mapByIds(List<Long> idList) {
        Map<Long, PlayerInfo> map = new HashMap<>();

        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(PlayerInfo::getId, idList);
        List<PlayerInfo> list = list(queryWrapper);

        if (CollectionUtils.isEmpty(list)){
            return map;
        }

        map = list.stream().collect(Collectors.toMap(PlayerInfo::getId, playerInfo -> playerInfo));

        return map;
    }

    @Override
    public PlayerInfo getBaseInfoById(Long id) {
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(PlayerInfo::getName, PlayerInfo::getAccount, PlayerInfo::getLevel, PlayerInfo::getAvatarPath)
                .eq(PlayerInfo::getId, id);
        return getOne(queryWrapper);
    }

    @Override
    public List<PlayerInfo> queryList(boolean isBot) {
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PlayerInfo::getIsBot, isBot);
        return list(queryWrapper);
    }

    @Override
    public Map<Long, PlayerInfo> playerIdMapPlayer(List<Long> playerIdList) {
        Map<Long, PlayerInfo> map = new HashMap<>();
        QueryWrapper<PlayerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(PlayerInfo::getId, playerIdList);
        List<PlayerInfo> list = list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return map;
        }

        for(PlayerInfo playerInfo: list){
            map.put(playerInfo.getId(), playerInfo);
        }

        return map;
    }

    //创建登录token
    public PlayerTokenResp createLoginToken(PlayerInfo playerInfo) {
        String tokenId = GenerateTools.createTokenId();
        PlayerTokenResp playerTokenResp = BeanUtil.toBean(playerInfo, PlayerTokenResp.class);
        playerTokenResp.setId(playerInfo.getId());
        playerTokenResp.setTokenId(tokenId);
        playerTokenResp.setLoginTime(LocalDateTime.now());
        playerTokenResp.setAvatarPath(playerInfo.getAvatarPath());
        playerTokenResp.setLevel(playerInfo.getLevel());
        playerTokenResp.setAddress(playerInfo.getAddress());
        ehcacheService.playerTokenCache().put(tokenId, playerTokenResp);
        log.info("生成 tokenCache:{}", tokenId);

        PlayerInfoResp playerInfoResp = BeanUtil.toBean(playerInfo, PlayerInfoResp.class);

        assembly(playerTokenResp, playerInfoResp);

        ehcacheService.playerInfoCache().put(playerInfo.getId().toString(), playerInfoResp);
        return playerTokenResp;
    }

    @Override
    public PlayerTokenResp updateLoginToken(String token, Long playerId) {
        PlayerInfo playerInfo = getById(playerId);

        PlayerTokenResp playerTokenResp = BeanUtil.toBean(playerInfo, PlayerTokenResp.class);
        playerTokenResp.setId(playerInfo.getId());
        playerTokenResp.setTokenId(token);
        playerTokenResp.setLoginTime(LocalDateTime.now());
        playerTokenResp.setAvatarPath(playerInfo.getAvatarPath());
        playerTokenResp.setLevel(playerInfo.getLevel());
        playerTokenResp.setAddress(playerInfo.getAddress());
        ehcacheService.playerTokenCache().put(token, playerTokenResp);
        log.info("更新tokenCache:{}", token);

        PlayerInfoResp playerInfoResp = BeanUtil.toBean(playerInfo, PlayerInfoResp.class);

        assembly(playerTokenResp, playerInfoResp);

        ehcacheService.playerInfoCache().put(playerInfo.getId().toString(), playerInfoResp);
        return playerTokenResp;
    }


    void assembly(PlayerTokenResp playerTokenResp, PlayerInfoResp playerInfoResp){
        //拼装统计关注 粉丝 点赞数量
        interactiveStatisticsService.assemblyBaseAndStatistics(playerInfoResp);
        //拼装统计私信未读情况 系统消息未读情况 回复评论未读情况

        int commentUnreadCount = commentService.unreadCount(playerTokenResp.getId());
        playerTokenResp.setCommentMessageUnread(commentUnreadCount > 0);

        int systemUnreadCount = systemMessageService.unreadSystemMessage(playerTokenResp.getId());
        playerTokenResp.setSystemMessageUnread(systemUnreadCount > 0);

        int privateUnreadCount = privateChatService.unreadCount(playerTokenResp.getId());
        playerTokenResp.setPrivateMessageUnread(privateUnreadCount > 0);
    }

}
