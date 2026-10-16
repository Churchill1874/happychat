package com.ent.happychat.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.common.constant.enums.ViewsEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.entity.Vote;
import com.ent.happychat.entity.VoteOption;
import com.ent.happychat.entity.VoteRecord;
import com.ent.happychat.mapper.VoteMapper;
import com.ent.happychat.mapper.VoteOptionMapper;
import com.ent.happychat.mapper.VoteRecordMapper;
import com.ent.happychat.pojo.req.vote.VoteAddReq;
import com.ent.happychat.pojo.req.vote.VoteOptionReq;
import com.ent.happychat.pojo.req.vote.VotePageReq;
import com.ent.happychat.pojo.req.vote.VoteSubmitReq;
import com.ent.happychat.pojo.req.vote.VoteUpdateReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.pojo.resp.vote.VoteResp;
import com.ent.happychat.service.UploadRecordService;
import com.ent.happychat.service.ViewsRecordService;
import com.ent.happychat.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements VoteService {

    private static final int MIN_OPTIONS = 2;
    private static final int MAX_OPTIONS = 5;

    @Autowired
    private VoteOptionMapper voteOptionMapper;
    @Autowired
    private VoteRecordMapper voteRecordMapper;
    @Autowired
    private ViewsRecordService viewsRecordService;
    @Autowired
    private UploadRecordService uploadRecordService;

    // ===================== 查询 =====================

    @Override
    public IPage<VoteResp> queryPage(VotePageReq req, boolean onlyVisible) {
        IPage<Vote> iPage = new Page<>(req.getPageNum(), req.getPageSize());
        LocalDateTime now = LocalDateTime.now();

        QueryWrapper<Vote> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(req.getId() != null, Vote::getId, req.getId())
            .like(StringUtils.isNotBlank(req.getTitle()), Vote::getTitle, req.getTitle())
            .eq(req.getIsTop() != null, Vote::getIsTop, req.getIsTop())
            .eq(onlyVisible, Vote::getStatus, true)
            .eq(!onlyVisible && req.getStatus() != null, Vote::getStatus, req.getStatus())
            .lt(Boolean.TRUE.equals(req.getEnded()), Vote::getDeadline, now)
            .ge(Boolean.FALSE.equals(req.getEnded()), Vote::getDeadline, now)
            .orderByDesc(Vote::getIsTop)
            .orderByDesc(Vote::getCreateTime);
        iPage = page(iPage, queryWrapper);

        List<Long> voteIds = iPage.getRecords().stream().map(Vote::getId).collect(Collectors.toList());
        Map<Long, List<VoteOption>> optionGroup = optionGroupByVoteIds(voteIds);

        IPage<VoteResp> respPage = new Page<>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal());
        List<VoteResp> records = new ArrayList<>();
        for (Vote vote : iPage.getRecords()) {
            records.add(toResp(vote, optionGroup.getOrDefault(vote.getId(), new ArrayList<>()), null, now));
        }
        respPage.setRecords(records);
        return respPage;
    }

    @Override
    public VoteResp detail(Long id, Long playerId) {
        Vote vote = getById(id);
        if (vote == null) {
            throw new DataException("投票不存在或已删除");
        }
        List<Long> myOptionIds = null;
        if (playerId != null) {
            myOptionIds = myRecords(id, playerId).stream().map(VoteRecord::getOptionId).collect(Collectors.toList());
        }
        return toResp(vote, listOptions(id), myOptionIds, LocalDateTime.now());
    }

    @Override
    public VoteResp findByIdAndInsertRecord(String ip, Long id, Long playerId, String playerName) {
        VoteResp resp = detail(id, playerId);
        if (!Boolean.TRUE.equals(resp.getStatus())) {
            throw new DataException("投票不存在或已下架");
        }
        increaseViewsCount(ip, id, resp.getTitle(), playerId, playerName);
        return resp;
    }

    @Override
    public Map<Long, Vote> mapByIds(List<Long> ids) {
        Map<Long, Vote> map = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return map;
        }
        QueryWrapper<Vote> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Vote::getId, Vote::getTitle).in(Vote::getId, ids);
        list(queryWrapper).forEach(vote -> map.put(vote.getId(), vote));
        return map;
    }

    // ===================== 后台维护 =====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(VoteAddReq req) {
        checkOptions(req.getOptions());

        Vote vote = new Vote();
        fillVote(vote, req);
        vote.setVoterCount(0);
        vote.setViewCount(0);
        vote.setCommentsCount(0);
        vote.setCreateTime(LocalDateTime.now());
        vote.setCreateName(TokenTools.getAdminName());
        save(vote);

        for (int i = 0; i < req.getOptions().size(); i++) {
            insertOption(vote.getId(), req.getOptions().get(i), i, vote.getCreateName());
        }

        uploadRecordService.cleanByPath(vote.getCoverImagePath());
        req.getOptions().forEach(o -> uploadRecordService.cleanByPath(o.getImagePath()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VoteUpdateReq req) {
        checkOptions(req.getOptions());

        Vote vote = getById(req.getId());
        if (vote == null) {
            throw new DataException("投票不存在或已删除");
        }
        String oldCover = vote.getCoverImagePath();

        fillVote(vote, req);
        vote.setUpdateTime(LocalDateTime.now());
        vote.setUpdateName(TokenTools.getAdminName());
        updateById(vote);

        // 处理选项：有 id 的修改，无 id 的新增，原有但没传的删除
        List<VoteOption> existing = listOptions(vote.getId());
        Map<Long, VoteOption> existMap = existing.stream().collect(Collectors.toMap(VoteOption::getId, o -> o));

        Set<Long> keepIds = new HashSet<>();
        for (VoteOptionReq o : req.getOptions()) {
            if (o.getId() != null) {
                if (!existMap.containsKey(o.getId())) {
                    throw new DataException("选项不属于该投票: " + o.getId());
                }
                keepIds.add(o.getId());
            }
        }

        List<VoteOption> removed = existing.stream().filter(o -> !keepIds.contains(o.getId())).collect(Collectors.toList());
        List<String> filesToRemove = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(removed)) {
            List<Long> removedIds = removed.stream().map(VoteOption::getId).collect(Collectors.toList());
            QueryWrapper<VoteRecord> recordWrapper = new QueryWrapper<>();
            recordWrapper.lambda().in(VoteRecord::getOptionId, removedIds);
            voteRecordMapper.delete(recordWrapper);
            voteOptionMapper.deleteBatchIds(removedIds);
            removed.forEach(o -> filesToRemove.add(o.getImagePath()));
            // 删除选项后参与人数可能变化，重新统计
            baseMapper.recountVoterCount(vote.getId());
        }

        String adminName = vote.getUpdateName();
        for (int i = 0; i < req.getOptions().size(); i++) {
            VoteOptionReq o = req.getOptions().get(i);
            if (o.getId() == null) {
                insertOption(vote.getId(), o, i, adminName);
            } else {
                VoteOption old = existMap.get(o.getId());
                if (!Objects.equals(old.getImagePath(), o.getImagePath())) {
                    filesToRemove.add(old.getImagePath());
                }
                VoteOption update = new VoteOption();
                update.setId(o.getId());
                update.setContent(o.getContent().trim());
                update.setImagePath(StringUtils.defaultString(o.getImagePath()));
                update.setSort(i);
                voteOptionMapper.updateById(update);
            }
        }

        if (!Objects.equals(oldCover, vote.getCoverImagePath())) {
            filesToRemove.add(oldCover);
        }

        uploadRecordService.cleanByPath(vote.getCoverImagePath());
        req.getOptions().forEach(o -> uploadRecordService.cleanByPath(o.getImagePath()));
        filesToRemove.stream().filter(StringUtils::isNotBlank).forEach(uploadRecordService::cleanRemoveFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Vote vote = getById(id);
        if (vote == null) {
            return;
        }
        List<VoteOption> options = listOptions(id);

        QueryWrapper<VoteRecord> recordWrapper = new QueryWrapper<>();
        recordWrapper.lambda().eq(VoteRecord::getVoteId, id);
        voteRecordMapper.delete(recordWrapper);

        QueryWrapper<VoteOption> optionWrapper = new QueryWrapper<>();
        optionWrapper.lambda().eq(VoteOption::getVoteId, id);
        voteOptionMapper.delete(optionWrapper);

        removeById(id);

        uploadRecordService.cleanRemoveFile(vote.getCoverImagePath());
        options.forEach(o -> uploadRecordService.cleanRemoveFile(o.getImagePath()));
    }

    // ===================== 前台投票 =====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VoteResp submit(VoteSubmitReq req, PlayerTokenResp player) {
        Vote vote = getById(req.getVoteId());
        if (vote == null || !Boolean.TRUE.equals(vote.getStatus())) {
            throw new DataException("投票不存在或已下架");
        }
        LocalDateTime now = LocalDateTime.now();
        if (vote.getDeadline() == null || !vote.getDeadline().isAfter(now)) {
            throw new DataException("投票已截止");
        }

        Set<Long> newIds = new LinkedHashSet<>(req.getOptionIds());
        newIds.remove(null);
        if (newIds.isEmpty()) {
            throw new DataException("请选择投票选项");
        }
        if (!Boolean.TRUE.equals(vote.getMultiSelect()) && newIds.size() != 1) {
            throw new DataException("该投票为单选，只能选择一个选项");
        }

        Set<Long> validIds = listOptions(vote.getId()).stream().map(VoteOption::getId).collect(Collectors.toSet());
        if (!validIds.containsAll(newIds)) {
            throw new DataException("投票选项不存在，请刷新后重试");
        }

        List<VoteRecord> oldRecords = myRecords(vote.getId(), player.getId());
        Set<Long> oldIds = oldRecords.stream().map(VoteRecord::getOptionId).collect(Collectors.toSet());

        if (!oldIds.equals(newIds)) {
            // 取消的选项
            List<Long> toRemove = oldIds.stream().filter(o -> !newIds.contains(o)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(toRemove)) {
                QueryWrapper<VoteRecord> wrapper = new QueryWrapper<>();
                wrapper.lambda()
                    .eq(VoteRecord::getVoteId, vote.getId())
                    .eq(VoteRecord::getPlayerId, player.getId())
                    .in(VoteRecord::getOptionId, toRemove);
                voteRecordMapper.delete(wrapper);
                toRemove.forEach(optionId -> voteOptionMapper.changeVoteCount(optionId, -1));
            }

            // 新选的选项
            for (Long optionId : newIds) {
                if (oldIds.contains(optionId)) {
                    continue;
                }
                VoteRecord record = new VoteRecord();
                record.setVoteId(vote.getId());
                record.setOptionId(optionId);
                record.setPlayerId(player.getId());
                record.setCreateTime(now);
                record.setCreateName(player.getName());
                voteRecordMapper.insert(record);
                voteOptionMapper.changeVoteCount(optionId, 1);
            }

            // 第一次投票，参与人数变化
            if (oldIds.isEmpty()) {
                baseMapper.recountVoterCount(vote.getId());
            }
        }

        return detail(vote.getId(), player.getId());
    }

    // ===================== 计数 =====================

    @Override
    public void increaseCommentsCount(Long id) {
        baseMapper.increaseCommentsCount(id);
    }

    @Async
    @Override
    public void increaseViewsCount(String ip, Long id, String content, Long playerId, String playerName) {
        viewsRecordService.addViewsRecord(ip, id, content, playerId, playerName, ViewsEnum.VOTE);
        baseMapper.increaseViewsCount(id);
    }

    // ===================== 内部方法 =====================

    private void checkOptions(List<VoteOptionReq> options) {
        if (options == null || options.size() < MIN_OPTIONS || options.size() > MAX_OPTIONS) {
            throw new DataException("投票选项至少2个，最多5个");
        }
        Set<String> contents = new HashSet<>();
        for (VoteOptionReq o : options) {
            if (StringUtils.isBlank(o.getContent())) {
                throw new DataException("选项内容不能为空");
            }
            if (!contents.add(o.getContent().trim())) {
                throw new DataException("选项内容不能重复: " + o.getContent().trim());
            }
        }
    }

    private void fillVote(Vote vote, VoteAddReq req) {
        vote.setTitle(req.getTitle().trim());
        vote.setDescription(req.getDescription());
        vote.setCoverImagePath(StringUtils.defaultString(req.getCoverImagePath()));
        vote.setMultiSelect(Boolean.TRUE.equals(req.getMultiSelect()));
        vote.setDeadline(req.getDeadline());
        vote.setIsTop(Boolean.TRUE.equals(req.getIsTop()));
        vote.setStatus(req.getStatus() == null || req.getStatus());
    }

    private void insertOption(Long voteId, VoteOptionReq req, int sort, String createName) {
        VoteOption option = new VoteOption();
        option.setVoteId(voteId);
        option.setContent(req.getContent().trim());
        option.setImagePath(StringUtils.defaultString(req.getImagePath()));
        option.setSort(sort);
        option.setVoteCount(0);
        option.setCreateTime(LocalDateTime.now());
        option.setCreateName(createName);
        voteOptionMapper.insert(option);
    }

    private List<VoteOption> listOptions(Long voteId) {
        QueryWrapper<VoteOption> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(VoteOption::getVoteId, voteId).orderByAsc(VoteOption::getSort).orderByAsc(VoteOption::getId);
        return voteOptionMapper.selectList(wrapper);
    }

    private Map<Long, List<VoteOption>> optionGroupByVoteIds(List<Long> voteIds) {
        if (CollectionUtils.isEmpty(voteIds)) {
            return new HashMap<>();
        }
        QueryWrapper<VoteOption> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(VoteOption::getVoteId, voteIds).orderByAsc(VoteOption::getSort).orderByAsc(VoteOption::getId);
        return voteOptionMapper.selectList(wrapper).stream().collect(Collectors.groupingBy(VoteOption::getVoteId));
    }

    private List<VoteRecord> myRecords(Long voteId, Long playerId) {
        QueryWrapper<VoteRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(VoteRecord::getVoteId, voteId).eq(VoteRecord::getPlayerId, playerId);
        return voteRecordMapper.selectList(wrapper);
    }

    private VoteResp toResp(Vote vote, List<VoteOption> options, List<Long> myOptionIds, LocalDateTime now) {
        VoteResp resp = BeanUtil.toBean(vote, VoteResp.class);
        resp.setEnded(vote.getDeadline() == null || !vote.getDeadline().isAfter(now));
        resp.setOptions(options);
        resp.setMyOptionIds(myOptionIds == null ? new ArrayList<>() : myOptionIds);
        return resp;
    }

}
