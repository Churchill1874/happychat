package com.ent.happychat.config;

import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class EhcacheConfig {

    @Bean
    public org.ehcache.CacheManager ehCacheManager() {
        URL url = getClass().getResource("/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(url);
        org.ehcache.CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        return cacheManager;
    }

}
