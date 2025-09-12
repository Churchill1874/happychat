package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Tieba;
import com.ent.happychat.pojo.req.PageBase;

public interface TiebaService extends IService<Tieba> {

    Tieba findById(Long id);

    IPage<Tieba> queryPage(PageBase pageBase, Long playerId, String title);

    void send(Tieba tieba);

    void updateLastCommentTime(Long id);
}
