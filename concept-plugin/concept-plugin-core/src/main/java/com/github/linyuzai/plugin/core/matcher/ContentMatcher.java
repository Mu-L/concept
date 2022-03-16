package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.BytesPluginResolver;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@DependOnResolvers(BytesPluginResolver.class)
public abstract class ContentMatcher<T> extends GenericTypePluginMatcher<T> {

    private Charset charset;

    public ContentMatcher(String charset) {
        this.charset = Charset.forName(charset);
    }

    @Override
    public boolean tryMatch(PluginContext context, Type type, Annotation[] annotations) {
        if (type == byte[].class) {
            Metadata metadata = new Metadata();
            metadata.setTarget(byte[].class);
            return setMatchedValueWithBytes(context, metadata, byte[].class);
        }
        return super.tryMatch(context, type, annotations);
    }

    @Override
    public boolean tryMatch(PluginContext context, Metadata metadata, Annotation[] annotations) {
        Type target = metadata.getTarget();
        if (target instanceof Class) {
            Class<?> clazz = (Class<?>) target;
            return setMatchedValueWithBytes(context, metadata, clazz);
        } else if (target instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) target).getRawType();
            Class<?> toClass = toClass(rawType);
            if (toClass != null) {
                return setMatchedValueWithBytes(context, metadata, toClass);
            }
        } else if (target instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) target).getUpperBounds();
            if (upperBounds.length > 0) {
                Class<?> toClass = toClass(upperBounds[0]);
                if (toClass != null) {
                    return setMatchedValueWithBytes(context, metadata, toClass);
                }
            }
        }
        return false;
    }

    public boolean setMatchedValueWithBytes(PluginContext context, Metadata metadata, Class<?> target) {
        Map<String, byte[]> bytes = context.get(Plugin.BYTES);
        Map<String, ?> contents;
        if (byte[].class == target) {
            contents = bytes;
        } else if (InputStream.class == target) {
            contents = bytes.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, it ->
                            new ByteArrayInputStream(it.getValue())));
        } else if (String.class == target) {
            contents = bytes.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, it ->
                            charset == null ? new String(it.getValue()) :
                                    new String(it.getValue(), charset)));
        } else {
            return false;
        }
        return setMatchedValue(context, metadata, contents, target, "content");
    }
}