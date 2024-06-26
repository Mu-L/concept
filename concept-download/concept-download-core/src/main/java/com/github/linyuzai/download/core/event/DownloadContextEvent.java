package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 附带了 {@link DownloadContext} 的 {@link DownloadEvent}。
 */
@Getter
@RequiredArgsConstructor
public abstract class DownloadContextEvent extends AbstractDownloadEvent {

    /**
     * 下载上下文
     */
    @NonNull
    private final DownloadContext context;
}
