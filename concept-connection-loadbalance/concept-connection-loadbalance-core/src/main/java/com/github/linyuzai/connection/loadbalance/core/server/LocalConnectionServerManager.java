package com.github.linyuzai.connection.loadbalance.core.server;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 连接服务管理器简单实现。
 * <p>
 * Simple impl of server manager.
 */
@Getter
@RequiredArgsConstructor
public class LocalConnectionServerManager implements ConnectionServerManager {

    private final List<ConnectionServer> connectionServers = new CopyOnWriteArrayList<>();

    private final ConnectionServer local;

    @Override
    public void add(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        connectionServers.add(server);
    }

    @Override
    public void remove(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        connectionServers.remove(server);
    }

    @Override
    public void clear(ConnectionLoadBalanceConcept concept) {
        connectionServers.clear();
    }

    @Override
    public ConnectionServer getLocal(ConnectionLoadBalanceConcept concept) {
        return local;
    }

    @Override
    public List<ConnectionServer> getConnectionServers(ConnectionLoadBalanceConcept concept) {
        return Collections.unmodifiableList(connectionServers);
    }
}
