package com.github.linyuzai.download.autoconfigure.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.Charset;

/**
 * 匹配 {@link ClassPathResource} 对象的 {@link SourceFactory}。
 */
public class ClassPathSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof ClassPathResource;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        DownloadOptions options = DownloadOptions.get(context);
        Charset charset = options.getCharset();
        return new ClassPathSource.Builder<>()
                .resource((ClassPathResource) source)
                .charset(charset)
                .build();
    }
}
