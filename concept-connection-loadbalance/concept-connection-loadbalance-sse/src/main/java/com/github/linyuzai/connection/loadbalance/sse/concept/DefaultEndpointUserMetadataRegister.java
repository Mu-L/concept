package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.extension.UserMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.UserSelector;

/**
 * userId 注册器。
 * 配合 {@link UserMessage} {@link UserSelector} 使用。
 * <p>
 * userId register.
 * Work with {@link UserMessage} {@link UserSelector}.
 */
public class DefaultEndpointUserMetadataRegister implements SseLifecycleListener {

    public static final String NAME = "userId";

    @Override
    public void onEstablish(Connection connection, ConnectionLoadBalanceConcept concept) {
        if (connection instanceof SseConnection) {
            Object userId = connection.getMetadata().get(NAME);
            if (userId != null) {
                connection.getMetadata().put(UserSelector.KEY, userId);
            }
        }
    }

    @Override
    public void onClose(Connection connection, Object reason, ConnectionLoadBalanceConcept concept) {

    }
}
