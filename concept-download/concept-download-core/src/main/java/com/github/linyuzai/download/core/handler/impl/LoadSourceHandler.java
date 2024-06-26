package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.load.SourceLoadedEvent;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 对所有的 {@link Source} 进行加载。
 */
@Getter
@RequiredArgsConstructor
public class LoadSourceHandler implements DownloadHandler {

    /**
     * 加载器
     */
    private final SourceLoader sourceLoader;

    /**
     * 加载 {@link Source}。
     * 使用 {@link SourceLoader} 加载所有的 {@link Source}，
     * 发布 {@link SourceLoadedEvent} 事件，
     * 设置新的 {@link Source} 到 {@link DownloadContext} 中。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        Source source = context.get(Source.class);
        if (source == null) {
            return chain.next(context);
        }
        DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        /*if (sourceLoader instanceof ReactiveSourceLoader) {
            return ((ReactiveSourceLoader) sourceLoader).load(source, context, () ->
                    chain.next(context));
        } else {
            sourceLoader.load(source, context);
            publisher.publish(new SourceLoadedEvent(context, source));
            return chain.next(context);
        }*/
        sourceLoader.load(source, context);
        publisher.publish(new SourceLoadedEvent(context, source));
        return chain.next(context);
    }
}
