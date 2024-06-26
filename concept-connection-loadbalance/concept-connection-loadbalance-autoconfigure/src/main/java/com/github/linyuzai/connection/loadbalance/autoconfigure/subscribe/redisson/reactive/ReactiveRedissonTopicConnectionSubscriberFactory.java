package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redisson.reactive;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonReactiveClient;

/**
 * Redisson (Shared) Topic 连接订阅器工厂。
 * <p>
 * Factory of {@link ReactiveRedissonTopicConnectionSubscriber}.
 */
@Getter
@Setter
public class ReactiveRedissonTopicConnectionSubscriberFactory
        extends AbstractMasterSlaveConnectionSubscriberFactory {

    private RedissonReactiveClient redissonReactiveClient;

    private boolean shared;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new ReactiveRedissonTopicConnectionSubscriber(redissonReactiveClient, shared);
    }
}
