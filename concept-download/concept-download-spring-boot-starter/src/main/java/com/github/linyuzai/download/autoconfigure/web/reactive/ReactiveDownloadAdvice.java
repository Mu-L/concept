package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Getter
@Setter
@RestControllerAdvice
public class ReactiveDownloadAdvice implements HandlerResultHandler, EmbeddedValueResolverAware, Ordered {

    private final DownloadConcept concept;

    private final DownloadProperties properties;

    private StringValueResolver resolver;

    private int order;

    public ReactiveDownloadAdvice(DownloadConcept concept, DownloadProperties properties) {
        this.concept = concept;
        this.properties = properties;
        setOrder(getOrder() - 1);
    }

    @Override
    public boolean supports(HandlerResult result) {
        /*MethodParameter returnType = result.getReturnTypeSource();
        Class<?> containingClass = returnType.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, Download.class) ||
                returnType.hasMethodAnnotation(Download.class));*/
        MethodParameter returnType = result.getReturnTypeSource();
        return returnType.hasMethodAnnotation(Download.class);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Mono<Void> handleResult(@NonNull ServerWebExchange exchange, @NonNull HandlerResult result) {
        MethodParameter returnType = result.getReturnTypeSource();
        Object returnValue = result.getReturnValue();
        DownloadOptions options = properties.toOptions(returnType, returnValue,
                getRequest(exchange), getResponse(exchange), resolver);
        return (Mono<Void>) concept.download(options);
    }

    @Deprecated
    protected HandlerMethod getHandlerMethod(ServerWebExchange exchange) {
        Object attribute = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (attribute instanceof HandlerMethod) {
            return (HandlerMethod) attribute;
        }
        return null;
    }

    protected DownloadRequest getRequest(ServerWebExchange exchange) {
        return new ReactiveDownloadRequest(exchange.getRequest());
    }

    protected DownloadResponse getResponse(ServerWebExchange exchange) {
        return new ReactiveDownloadResponse(exchange.getResponse());
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }
}
