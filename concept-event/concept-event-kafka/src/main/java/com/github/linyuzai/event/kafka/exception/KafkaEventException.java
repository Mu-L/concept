package com.github.linyuzai.event.kafka.exception;

import com.github.linyuzai.event.core.exception.EventException;

/**
 * Kafka 事件异常
 */
public class KafkaEventException extends EventException {

    public KafkaEventException(String message) {
        super(message);
    }

    public KafkaEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
