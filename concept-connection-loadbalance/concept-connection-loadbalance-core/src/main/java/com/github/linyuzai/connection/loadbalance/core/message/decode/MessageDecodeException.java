package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

/**
 * 消息解码异常。
 * <p>
 * Exception for message decode error.
 */
public class MessageDecodeException extends ConnectionLoadBalanceException {

    public MessageDecodeException(Object message) {
        super("Can not decode message: " + message);
    }
}
