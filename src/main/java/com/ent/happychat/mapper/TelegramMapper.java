// 目标路径: src/main/java/com/ent/happychat/mapper/TelegramMapper.java
package com.ent.happychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ent.happychat.entity.Telegram;

// 不涉及自增计数等自定义SQL，直接用 MyBatis-Plus 提供的默认 CRUD 即可，
// 不需要额外的 TelegramMapper.xml
public interface TelegramMapper extends BaseMapper<Telegram> {
}
