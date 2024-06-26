package com.github.linyuzai.connection.loadbalance.websocket.concept;

/**
 * 可以对默认的端点进行扩展配置。
 * 针对 web 和 webflux 会回调不同的配置对象。
 * <p>
 * You can extend the default endpoint configuration.
 * Different configuration objects will be called back for web and webflux.
 */
public interface DefaultEndpointCustomizer {

    void customize(Object config);
}
