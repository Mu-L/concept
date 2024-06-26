package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.EventBus;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exception.EventException;
import com.github.linyuzai.event.core.exchange.EndpointExchange;
import com.github.linyuzai.event.core.exchange.EventExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 事件总线配置类
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(EventBusProperties.class)
public class EventBusConfiguration {

    /**
     * 'concept.event.bus.enabled=true' 时注入事件总线
     * <p>
     * 基于 {@link ApplicationEventPublisher} 的事件发布和监听
     */
    @Bean(initMethod = "initialize", destroyMethod = "destroy")
    @ConditionalOnProperty(name = "concept.event.bus.enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public EventBus eventBus(EventBusProperties properties, EventConcept concept,
                             ApplicationEventPublisher publisher, List<EventBusConfigurer> configurers) {
        EventExchange exchange = getEventExchange(concept, properties);
        ApplicationEventBus bus = new ApplicationEventBus(concept, exchange, publisher);
        configurers.forEach(it -> it.configure(bus));
        return bus;
    }

    /**
     * 获得事件总线的事件交换机
     * <p>
     * 通过 'concept.event.bus.engine' 和 'concept.event.bus.endpoint'
     * <p>
     * 来指定一个端点作为事件总线的中间件
     */
    private EventExchange getEventExchange(EventConcept concept, EventBusProperties properties) {
        String engine;
        if (StringUtils.hasText(properties.getEngine())) {
            engine = properties.getEngine();
        } else {
            Collection<? extends EventEngine> engines = concept.getEngines();
            int size = engines.size();
            if (size == 0) {
                throw new EventException("No engine found");
            } else if (size > 1) {
                throw new EventException("More than one engine found");
            } else {
                engine = engines.iterator().next().getName();
            }
        }

        String endpoint;
        if (StringUtils.hasText(properties.getEndpoint())) {
            endpoint = properties.getEndpoint();
        } else {
            Collection<? extends EventEndpoint> endpoints = concept.getEngine(engine).getEndpoints();
            int size = endpoints.size();
            if (size == 0) {
                throw new EventException("No endpoint found");
            } else if (size > 1) {
                throw new EventException("More than one endpoint found");
            } else {
                endpoint = endpoints.iterator().next().getName();
            }
        }

        return new EndpointExchange(engine, endpoint);
    }
}
