package com.ent.happychat.common.tools;

import cn.hutool.core.util.RandomUtil;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 生产工具
 */
public class GenerateTools {
    /**
     * 根据当前时间生成编号
     *
     * @return
     */
    public static String getTimeNo() {
        return LocalDateTime.now().toString()
                .replace(".", "")
                .replace("T", "")
                .replace(":", "")
                .replace("-", "");
    }

    /**
     * 生成五位验证码
     */
    public static String getVerificationCode() {
        int num = RandomUtil.randomInt(10000, 99999);
        return String.valueOf(num);
    }


    /**
     * 生产警告日志对象
     *
     * @param message
     * @return
     */


    /**
     * 生成token对象
     *
     * @param user
     * @return
     */
/*    public static PlayerToken createToken(Player user) {
        PlayerToken playerToken = new PlayerToken();
        playerToken.setName(user.getName());
        playerToken.setLoginTime(LocalDateTime.now());
        playerToken.setAccount(user.getAccount());
        playerToken.setId(user.getId());
        playerToken.setLevel(user.getLevel());
        return playerToken;
    }*/


    /**
     * 创建tokenId
     *
     * @return
     */
    public static String createTokenId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
