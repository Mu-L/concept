package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kafka Topic 连接订阅器。
 * 通过 {@link KafkaTemplate} 转发消息，通过 {@link KafkaListenerContainerFactory} 订阅消息。
 * <p>
 * {@link ConnectionSubscriber} impl by Kafka.
 * Forward message by {@link KafkaTemplate}, listen message by {@link KafkaListenerContainerFactory}.
 */
@Getter
@RequiredArgsConstructor
public class KafkaTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final KafkaTemplate<?, Object> kafkaTemplate;

    private final KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory;

    /**
     * 创建 Kafka Topic 的监听连接。
     * <p>
     * Create the connection to listen message from Kafka.
     */
    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        KafkaTopicSubscriberConnection connection = new KafkaTopicSubscriberConnection();
        connection.setId(id);
        KafkaListenerEndpoint endpoint = new GroupTopicKafkaListenerEndpoint(id, topic);
        MessageListenerContainer container = createMessageListenerContainer(endpoint);
        ContainerProperties.AckMode mode = container.getContainerProperties().getAckMode();
        Object listener;
        //手动确认
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            listener = (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) -> {
                onMessageReceived(connection, data);
                if (acknowledgment != null) {
                    acknowledgment.acknowledge();
                }
            };
        } else {
            listener = (MessageListener<Object, Object>) data ->
                    onMessageReceived(connection, data);
        }
        container.setupMessageListener(listener);
        container.start();
        connection.setContainer(container);
        return connection;
    }

    protected MessageListenerContainer createMessageListenerContainer(KafkaListenerEndpoint endpoint) {
        return kafkaListenerContainerFactory.createListenerContainer(endpoint);
    }

    /**
     * 创建 Kafka Topic 的转发连接。
     * <p>
     * Create the connection to forward message by Kafka.
     */
    @Override
    protected Connection createObservable(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        KafkaTopicObservableConnection connection = new KafkaTopicObservableConnection();
        connection.setId(id);
        connection.setTopic(topic);
        connection.setKafkaTemplate(kafkaTemplate);
        return connection;
    }

    @Override
    protected ConnectionServer getSubscribeServer() {
        return new KafkaConnectionServer(kafkaTemplate);
    }

    @Getter
    @RequiredArgsConstructor
    public static class KafkaConnectionServer implements ConnectionServer {

        private final KafkaTemplate<?, Object> kafkaTemplate;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "kafka";
        }

        @Override
        public String getHost() {
            Object o = kafkaTemplate.getProducerFactory().getConfigurationProperties()
                    .get("bootstrap.servers");
            if (o instanceof List) {
                return ((List<?>) o).stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining("&"));
            }
            return null;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public Map<String, String> getMetadata() {
            return null;
        }

        @Override
        public URI getUri() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }
    }
}
