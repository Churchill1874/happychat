package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.service.CommentService;
import com.ent.happychat.service.LevelProgressService;
import com.ent.happychat.service.LikesRecordService;
import com.ent.happychat.service.PlayerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LevelProgressServiceImpl implements LevelProgressService {

    @Autowired
    private PlayerInfoService playerInfoService;
    @Autowired
    private LikesRecordService likesRecordService;
    @Autowired
    private CommentService commentService;

    //todo 引入下注记录服务



    @Override
    public void levelProgress(Long targetPlayerId) {
        QueryWrapper<LikesRecord> queryTargetPlayer = new QueryWrapper<>();
        queryTargetPlayer.lambda()
                .eq(LikesRecord::getTargetPlayerId, targetPlayerId);
        //被点赞玩家被点赞总数
        int likesCount = likesRecordService.count(queryTargetPlayer);

        //todo 查询被点赞玩家的 下注正确总数

        //todo 如果下注猜对次数 和 被点赞次数大于或者等于 下一个等级的要求 就给被点赞的玩家 进级


    }

}
