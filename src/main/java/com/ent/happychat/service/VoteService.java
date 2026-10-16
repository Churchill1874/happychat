package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Vote;
import com.ent.happychat.pojo.req.vote.VoteAddReq;
import com.ent.happychat.pojo.req.vote.VotePageReq;
import com.ent.happychat.pojo.req.vote.VoteSubmitReq;
import com.ent.happychat.pojo.req.vote.VoteUpdateReq;
import com.ent.happychat.pojo.resp.player.PlayerTokenResp;
import com.ent.happychat.pojo.resp.vote.VoteResp;

import java.util.List;
import java.util.Map;

public interface VoteService extends IService<Vote> {

    /**
     * 分页查询，每条记录带选项
     *
     * @param req
     * @param onlyVisible 是否只查展示状态的（前台用）
     */
    IPage<VoteResp> queryPage(VotePageReq req, boolean onlyVisible);

    /**
     * 查询详情（带选项）
     *
     * @param id
     * @param playerId 当前用户id，传入时会返回该用户已选的选项
     */
    VoteResp detail(Long id, Long playerId);

    void add(VoteAddReq req);

    void update(VoteUpdateReq req);

    /**
     * 删除投票、选项、投票记录、图片文件
     */
    void delete(Long id);

    /**
     * 前台查看详情并插入浏览记录
     */
    VoteResp findByIdAndInsertRecord(String ip, Long id, Long playerId, String playerName);

    /**
     * 投票 / 改票
     */
    VoteResp submit(VoteSubmitReq req, PlayerTokenResp player);

    void increaseCommentsCount(Long id);

    void increaseViewsCount(String ip, Long id, String content, Long playerId, String playerName);

    Map<Long, Vote> mapByIds(List<Long> ids);

}
