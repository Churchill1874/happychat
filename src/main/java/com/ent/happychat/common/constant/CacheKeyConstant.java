package com.ent.happychat.common.constant;

/**
 * 缓存key
 */
public interface CacheKeyConstant {

    /**
     * 在线人数
     */
    String ONLINE_COUNT = "online_count";

    /**
     * 玩家在线随机人数
     */
    String PLAYER_ONLINE_COUNT = "player_online_count";

    /**
     * 玩家token
     */
    String PLAYER_TOKEN = "player_token";

    /**
     * 玩家信息
     */
    String PLAYER_INFO = "player_info";

    /**
     *管理员token
     */
    String ADMIN_TOKEN = "admin_token";

    /**
     *3秒锁
     */
    String LOCK_3_SECOND = "lock_3_Second";

    /**
     *验证码
     */
    String VERIFICATION_CODE = "verification_code";

    /**
     *黑名单
     */
    String BLACKLIST = "blacklist";

    /**
     * 首页新闻
     */
    String HOME_NEWS = "home_news";

    /**
     * 首页新闻Key
     */
    String HOME_NEWS_KEY = "home_news_key";

    String BLACKLIST_SET_KEY = "blacklist_set_key";

    /**
     * 重复冲求校验
     */
    String REPEATED_REQUEST_VERIFICATION = "repeated_request_verification";



}
