package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class NettyConnectionFactory extends AbstractConnectionFactory<NettyConnection> {

    public NettyConnectionFactory() {
        addScopes(NettyScoped.NAME);
    }

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof ChannelHandlerContext || o instanceof Channel;
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return new NettyConnection(getChannel(o), Connection.Type.CLIENT, metadata);
    }

    protected Channel getChannel(Object o) {
        if (o instanceof ChannelHandlerContext) {
            return ((ChannelHandlerContext) o).channel();
        } else if (o instanceof Channel) {
            return (Channel) o;
        } else {
            throw new IllegalArgumentException("Can not happen");
        }
    }
}