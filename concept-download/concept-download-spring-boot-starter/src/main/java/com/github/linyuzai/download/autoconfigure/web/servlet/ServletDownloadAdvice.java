package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Getter
@RequiredArgsConstructor
public class ServletDownloadAdvice implements ResponseBodyAdvice<Object> {

    private final DownloadConcept concept;

    private final DownloadProperties properties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(Download.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        DownloadOptions options = properties.toOptions(returnType, body);
        return concept.download(options);
    }
}