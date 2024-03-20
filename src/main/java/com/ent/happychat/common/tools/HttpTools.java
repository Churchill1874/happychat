package com.ent.happychat.common.tools;

import com.ent.happychat.common.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class HttpTools {

    private static final Map<String, Integer> urlMap = new HashMap<>();

    static {
        //初始化url和平台的对应关系
        urlMap.put("127.0.0.1", 0);
        urlMap.put("localhost", 0);

    }

    private static Ip2regionSearcher ip2regionSearcher;

    @Autowired
    public void setIp2regionSearcher(Ip2regionSearcher ip2regionSearcher){
        HttpTools.ip2regionSearcher = ip2regionSearcher;
    }

    /**
     * 根据ip获取地址
     * @param ip
     * @return
     */
    public static String findAddressByIp(String ip){
        return ip2regionSearcher.getAddress(ip);
    }

    /**
     * 获取地址
     * @return
     */
    public static String getAddress(){
        return findAddressByIp(getIp());
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取域名所在平台
     *
     * @return
     */
    public static int getPlatform() {
        String url = getRequest().getServerName();
        Integer platform = urlMap.get(url);
        if (platform == null) {
            log.error("未获取到域名:{},的平台", url);
            throw new DataException("未找到该域名的平台");
        }
        return platform;
    }

    /**
     * 获取请求头中的token
     *
     * @return
     */
    public static String getHeaderToken() {
        return getRequest().getHeader("TOKEN_ID");
    }

    /**
     * 解析http请求ip
     *
     * @return
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        String ip = null;
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddresses) || "0:0:0:0:0:0:0:1".equals(ipAddresses)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("获取local host信息异常:{}", e.getMessage());
                }
                ipAddresses = inet.getHostAddress();
            }
        }
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        return ip;
    }

}
