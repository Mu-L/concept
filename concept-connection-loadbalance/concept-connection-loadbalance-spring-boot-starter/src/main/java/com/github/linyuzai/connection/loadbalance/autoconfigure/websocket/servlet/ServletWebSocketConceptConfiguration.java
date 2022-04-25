package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketConnectionSubscriber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebSocketConceptConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        return new ServletWebSocketConnectionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionSubscriber connectionSubscriber() {
        return new ServletWebSocketConnectionSubscriber();
    }
}