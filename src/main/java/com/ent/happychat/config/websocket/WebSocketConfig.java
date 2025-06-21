package com.ent.happychat.config.websocket;

import com.ent.happychat.service.EhcacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final EhcacheService ehcacheService;

    // 构造函数注入 EhcacheService
    public WebSocketConfig(EhcacheService ehcacheService) {
        this.ehcacheService = ehcacheService;
    }

    @Bean
    public HttpHandshakeInterceptor httpHandshakeInterceptor() {
        return new HttpHandshakeInterceptor();
    }


    @Bean
    public CustomPrincipalHandshakeHandler customPrincipalHandshakeHandler() {
        return new CustomPrincipalHandshakeHandler(ehcacheService);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置消息代理
        registry.enableSimpleBroker("/topic", "/queue"); // 客户端订阅前缀
        registry.setApplicationDestinationPrefixes("/app"); // 客户端发送消息的前缀
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 配置 WebSocket 连接点
        registry.addEndpoint("/ws") // 前端连接 WebSocket 的端点
            .setAllowedOriginPatterns("*") // 允许跨域
            .addInterceptors(httpHandshakeInterceptor()) //注册自定义拦截器
            .setHandshakeHandler(customPrincipalHandshakeHandler()) // 注册自定义的 HandshakeHandler
            .withSockJS(); // 开启 SockJS 支持
    }

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncTask-");
        executor.initialize();
        return executor;
    }

}
