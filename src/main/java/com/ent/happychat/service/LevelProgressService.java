package com.ent.happychat.service;

public interface LevelProgressService {

    /**
     * 检查是否满足玩家晋级条件 并晋级
     * @param targetPlayerId
     */
    void levelProgress(Long targetPlayerId);

}
