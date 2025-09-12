package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.TiebaComment;
import com.ent.happychat.pojo.req.PageBase;

public interface TiebaCommentService extends IService<TiebaComment> {

    IPage<TiebaComment> queryPage(PageBase pageBase, Long playerId, Long tiebaId);

    void send(TiebaComment tiebaComment);

}
