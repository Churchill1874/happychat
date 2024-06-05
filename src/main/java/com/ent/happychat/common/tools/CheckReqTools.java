package com.ent.happychat.common.tools;

import com.ent.happychat.common.exception.DataException;

//校验工具类
public class CheckReqTools {

    public static void account(String account) {
        if (!account.matches("^[a-zA-Z0-9]+$")){
            throw new DataException("账号只能输入英文和数字");
        }
    }

    public static void name(String name) {
        if (!name.matches("^[a-zA-Z0-9]+([._ -]?[a-zA-Z0-9]+)*$")) {
            throw new DataException("昵称仅支持一位.或_或-或空格的特殊符号");
        }
    }

}
