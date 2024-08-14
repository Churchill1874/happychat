package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.common.constant.enums.LikesEnum;
import com.ent.happychat.entity.LikesRecord;
import com.ent.happychat.pojo.req.likes.LikesRecordPageReq;

public interface LikesRecordService extends IService<LikesRecord> {

    /**
     * 分页查询
     * @param po
     * @return
     */
    IPage<LikesRecord> queryPage(LikesRecordPageReq po);

    /**
     * 点赞
     * @param playerId
     * @param playerName
     * @param likesId
     * @param content
     */
    void increaseLikesCount(Long playerId, String playerName, Long likesId, String content, LikesEnum likesType, Long targetPlayerId);

}
