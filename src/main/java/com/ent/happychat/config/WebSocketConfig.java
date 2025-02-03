package com.ent.happychat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointConfig.Configurator configurator() {
        return new ServerEndpointConfig.Configurator();
    }
}
