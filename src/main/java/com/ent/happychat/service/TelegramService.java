// 目标路径: src/main/java/com/ent/happychat/service/TelegramService.java
package com.ent.happychat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ent.happychat.entity.Telegram;
import com.ent.happychat.pojo.req.telegram.TelegramPageReq;

public interface TelegramService extends IService<Telegram> {

    /**
     * 分页查询
     * @param req
     * @return
     */
    IPage<Telegram> queryPage(TelegramPageReq req);

    /**
     * 新增
     * @param telegram
     */
    void add(Telegram telegram);

}
