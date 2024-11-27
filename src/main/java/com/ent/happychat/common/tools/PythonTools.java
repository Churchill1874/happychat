package com.ent.happychat.common.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * python脚本访问工具类
 */
@Slf4j
public class PythonTools {


    /**
     * 请求球探网获取实时数据
     */
    public static boolean getLiveScore() {
        boolean result = qiuTanScore();
        log.info("执行爬虫球探网实时比分,结果:{}", result);
        return result;
    }

    //爬取球探网比分数据
    private static boolean qiuTanScore() {
        try {
            // python脚本在该项目中的相对路径
            String pythonPath = "D:\\project\\sports\\python\\qiutan_score_test.py";
            Process process = Runtime.getRuntime().exec("python " + pythonPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
                if (StringUtils.isNotBlank(line) && line.contains("object has no attribute")
                        || line.contains("unknown error")
                        || line.contains("SysTemMessage: timeout")
                        || line.contains("SSLError")) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }


}
