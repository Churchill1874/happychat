package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.CampEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TimeUtils;
import com.ent.happychat.entity.Comment;
import com.ent.happychat.entity.InteractiveStatistics;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.PlayerToken;
import com.ent.happychat.mapper.CommentMapper;
import com.ent.happychat.mapper.PlayerInfoMapper;
import com.ent.happychat.pojo.req.PageBase;
import com.ent.happychat.pojo.resp.report.RankInfoResp;
import com.ent.happychat.pojo.resp.report.RankReportResp;
import com.ent.happychat.pojo.resp.report.RegisterReportResp;
import com.ent.happychat.service.InteractiveStatisticsService;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.PlayerTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerInfoServiceImpl extends ServiceImpl<PlayerInfoMapper, PlayerInfo> implements PlayerInfoService {

    @Autowired
    private InteractiveStatisticsService interactiveStatisticsService;
    @Autowired
    private PlayerTokenService playerTokenService;
    @Autowired
    private CommentMapper commentMapper;


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
                .ge(pageBase.getStartTime() != null, PlayerInfo::getCreateTime, pageBase.getStartTime())
                .le(pageBase.getEndTime() != null, PlayerInfo::getCreateTime, pageBase.getEndTime())
                .eq(playerInfo.getCampType() != null, PlayerInfo::getCampType, playerInfo.getCampType())
                .eq(StringUtils.isNotBlank(playerInfo.getIp()), PlayerInfo::getIp, playerInfo.getIp())
                .orderByDesc(PlayerInfo::getCreateTime);
        return page(iPage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PlayerInfo playerInfo) {
        if (StringUtils.isBlank(playerInfo.getAvatarPath())) {
            playerInfo.setAvatarPath("1");
        }
        PlayerInfo queryInfo = findByAccount(playerInfo.getAccount());
        if (queryInfo != null) {
            throw new DataException("该账号已存在");
        }
        if (playerInfo.getCampType() == null) {
            playerInfo.setCampType(CampEnum.NO);
        }

        save(playerInfo);

        InteractiveStatistics interactiveStatistics = interactiveStatisticsService.findByPlayerId(playerInfo.getId());
        if (interactiveStatistics == null) {
            throw new DataException("刚刚网络繁忙,请稍后再次请求");
        }

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

        if (CollectionUtils.isEmpty(list)) {
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
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }

        for (PlayerInfo playerInfo : list) {
            map.put(playerInfo.getId(), playerInfo);
        }

        return map;
    }

    @Override
    public RegisterReportResp getRegisterReport() {
        LambdaQueryWrapper<PlayerInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ge(PlayerInfo::getCreateTime, TimeUtils.startOfLastMonth())
                .eq(PlayerInfo::getIsBot, false)
                .select(PlayerInfo::getId, PlayerInfo::getCreateTime);
        List<PlayerInfo> list = list(queryWrapper);

        if(CollectionUtils.isEmpty(list)){
            return new RegisterReportResp();
        }

        RegisterReportResp registerReportResp = new RegisterReportResp();

        for(PlayerInfo playerInfo : list){
            //上个月统计
            if(playerInfo.getCreateTime().isBefore(TimeUtils.startOfThisMonth())){
                registerReportResp.setLastMonthRegister(registerReportResp.getLastMonthRegister() + 1);
            }
            //本月统计
            if(playerInfo.getCreateTime().isAfter(TimeUtils.endOfLastMonth())){
                registerReportResp.setThisMonthRegister(registerReportResp.getThisMonthRegister() + 1);
            }
            //本周统计
            if(playerInfo.getCreateTime().isAfter(TimeUtils.endOfLastWeek())){
                registerReportResp.setThisWeekRegister(registerReportResp.getThisWeekRegister() + 1);
            }
            //上周统计
            if(!playerInfo.getCreateTime().isBefore(TimeUtils.startOfLastWeek()) && playerInfo.getCreateTime().isBefore(TimeUtils.endOfLastWeek())){
                registerReportResp.setLastWeekRegister(registerReportResp.getLastWeekRegister() + 1);
            }
            //今日统计
            if(!playerInfo.getCreateTime().isBefore(TimeUtils.startOfToday())){
                registerReportResp.setTodayRegister(registerReportResp.getTodayRegister() + 1);
            }
            //昨日统计
            if(!playerInfo.getCreateTime().isBefore(TimeUtils.startOfYesterday()) && playerInfo.getCreateTime().isBefore(TimeUtils.endOfYesterday())){
                registerReportResp.setYesterdayRegister(registerReportResp.getYesterdayRegister() + 1);
            }
        }

        //开始计算
        registerReportResp.calcRates();

        //总计
        LambdaQueryWrapper<PlayerInfo> totalQueryWrapper = new LambdaQueryWrapper<>();
        totalQueryWrapper.eq(PlayerInfo::getIsBot, false);
        registerReportResp.setTotalRegister(count(totalQueryWrapper));
        return registerReportResp;
    }


    @Override
    public RankReportResp rankReport() {
        RankReportResp rankReportResp = new RankReportResp();

        LambdaQueryWrapper<PlayerInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(PlayerInfo::getIsBot, false)
                .select(PlayerInfo::getId, PlayerInfo::getName, PlayerInfo::getAvatarPath, PlayerInfo::getCreateTime, PlayerInfo::getAddress)
                .orderByDesc(PlayerInfo::getCreateTime)
                .last("LIMIT 10");

        List<PlayerInfo> playerInfoList = list(queryWrapper);
        if(CollectionUtils.isNotEmpty(playerInfoList)){
            List<RankInfoResp> registerList = new ArrayList<>();
            for(PlayerInfo playerInfo : playerInfoList){
                RankInfoResp rankInfoResp = new RankInfoResp();
                rankInfoResp.setName(playerInfo.getName());
                rankInfoResp.setAvatarPath(playerInfo.getAvatarPath());
                rankInfoResp.setPlayerId(playerInfo.getId());
                rankInfoResp.setAddress(playerInfo.getAddress());
                rankInfoResp.setCreateTime(playerInfo.getCreateTime());
                registerList.add(rankInfoResp);
            }
            rankReportResp.setRegisterList(registerList);
        }

        List<PlayerToken> playerTokenList = playerTokenService.loginList();
        if(CollectionUtils.isNotEmpty(playerTokenList)){
            List<RankInfoResp> loginList = new ArrayList<>();
            Set<Long> playerIdSet = playerTokenList.stream().map(PlayerToken::getPlayerId).collect(Collectors.toSet());
            Map<Long, PlayerInfo> map = mapByIds(new ArrayList<>(playerIdSet));

            for(PlayerToken playerToken: playerTokenList){
                RankInfoResp rankInfoResp = new RankInfoResp();
                PlayerInfo playerInfo = map.get(playerToken.getPlayerId());
                if(playerInfo != null){
                    rankInfoResp.setName(playerInfo.getName());
                    rankInfoResp.setAvatarPath(playerInfo.getAvatarPath());
                }
                rankInfoResp.setPlayerId(playerToken.getPlayerId());
                rankInfoResp.setAddress(playerToken.getCity());
                rankInfoResp.setCreateTime(playerToken.getLoginTime());
                loginList.add(rankInfoResp);
            }
            rankReportResp.setLoginList(loginList);
        }

        List<RankInfoResp> commentList = commentMapper.top10PlayerRank();
        if(CollectionUtils.isNotEmpty(commentList)){
            Set<Long> playerIdSet = commentList.stream().map(RankInfoResp::getPlayerId).collect(Collectors.toSet());
            Map<Long, PlayerInfo> map = mapByIds(new ArrayList<>(playerIdSet));

            for(RankInfoResp rankInfoResp: commentList){
                PlayerInfo playerInfo = map.get(rankInfoResp.getPlayerId());
                if(playerInfo != null){
                    rankInfoResp.setName(playerInfo.getName());
                    rankInfoResp.setAvatarPath(playerInfo.getAvatarPath());
                }
            }
        }

        rankReportResp.setCommentList(commentList);
        return rankReportResp;
    }

    @Override
    public int registerCountByIpOfToday(String ip) {
        LambdaQueryWrapper<PlayerInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(PlayerInfo::getIp, ip)
                .ge(PlayerInfo::getCreateTime, TimeUtils.startOfToday());
        return count(queryWrapper);
    }


}
