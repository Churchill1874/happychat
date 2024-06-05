package com.ent.happychat.common.tools;

import com.ent.happychat.common.exception.DataException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * 编码处理工具
 */
public class CodeTools {

    public static String md5AndSalt(String password, String salt){
        if (StringUtils.isBlank(password)){
            throw new DataException("加密的密码不能为空");
        }
        if (StringUtils.isBlank(salt)){
            throw new DataException("盐值不能为空");
        }
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }

}
