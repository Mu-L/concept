package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import com.github.linyuzai.connection.loadbalance.sse.concept.SubscriberSseConnection;
import lombok.Getter;
import lombok.Setter;
import okhttp3.sse.EventSource;

import java.util.function.Consumer;

@Deprecated
@Getter
@Setter
public class OkHttpSseConnection extends SubscriberSseConnection {

    private EventSource eventSource;

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (eventSource != null) {
                eventSource.cancel();
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
