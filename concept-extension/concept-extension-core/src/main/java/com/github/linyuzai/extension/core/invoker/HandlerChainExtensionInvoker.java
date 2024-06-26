package com.github.linyuzai.extension.core.invoker;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.concept.simple.SimpleArgumentAndResult;
import com.github.linyuzai.extension.core.factory.ExceptionResultFactory;
import com.github.linyuzai.extension.core.handler.ExtensionHandler;
import com.github.linyuzai.extension.core.handler.ExtensionHandlerChainImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class HandlerChainExtensionInvoker implements ExtensionInvoker {

    private final Extension extension;

    private final Extension.Argument argument;

    private final ExceptionResultFactory resultFactory;

    private final List<ExtensionHandler> handlers;

    @Override
    public Collection<Extension.ArgumentAndResult> invoke() {
        return invokeWithHandlerChain();
    }

    protected Collection<Extension.ArgumentAndResult> invokeWithHandlerChain() {
        try {
            return new ExtensionHandlerChainImpl(handlers).next(extension, argument);
        } catch (Throwable e) {
            Extension.Result result = resultFactory.create(e, argument, extension);
            return Collections.singletonList(new SimpleArgumentAndResult(extension, argument, result));
        }
    }
}
