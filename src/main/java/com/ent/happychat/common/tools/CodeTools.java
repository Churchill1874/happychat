package com.ent.happychat.common.tools;

import com.ent.happychat.common.exception.DataException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * 编码处理工具
 */
public class CodeTools {

    private static final String SALT = "fk";

    public static String md5AndSalt(String password){
        if (StringUtils.isBlank(password)){
            throw new DataException("加盐密码不能为空");
        }
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes());
    }


}
