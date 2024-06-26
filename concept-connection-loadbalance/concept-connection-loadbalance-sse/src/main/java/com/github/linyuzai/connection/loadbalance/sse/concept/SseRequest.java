package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionRequest;
import org.springframework.http.HttpRequest;

import java.net.InetSocketAddress;

/**
 * SSE 连接请求。
 * <p>
 * SSE request.
 *
 * @since 2.7.0
 */
public interface SseRequest extends ConnectionRequest, HttpRequest {

    /**
     * Return the address on which the request was received.
     */
    InetSocketAddress getLocalAddress();

    /**
     * Return the address of the remote client.
     */
    InetSocketAddress getRemoteAddress();
}
