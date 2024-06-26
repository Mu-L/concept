package com.github.linyuzai.domain.core.condition;

import com.github.linyuzai.domain.core.lambda.LambdaFunction;

import java.lang.invoke.SerializedLambda;
import java.util.Collection;

/**
 * 支持 lambda 的查询条件
 */
public class LambdaConditions extends Conditions {

    @Override
    public LambdaConditions lambda() {
        return this;
    }

    public <T, R> LambdaConditions equal(LambdaFunction<T, R> cf, Object value) {
        return equal(cf, value, false);
    }

    public <T, R> LambdaConditions equal(LambdaFunction<T, R> cf, Object value, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        equal(generate(sl, prependClass), value);
        return this;
    }

    public <T, R> LambdaConditions isNull(LambdaFunction<T, R> cf) {
        return isNull(cf, false);
    }

    public <T, R> LambdaConditions isNull(LambdaFunction<T, R> cf, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        isNull(generate(sl, prependClass));
        return this;
    }

    public <T, R> LambdaConditions isNotNull(LambdaFunction<T, R> cf) {
        return isNotNull(cf, false);
    }

    public <T, R> LambdaConditions isNotNull(LambdaFunction<T, R> cf, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        isNotNull(generate(sl, prependClass));
        return this;
    }

    public <T, R> LambdaConditions in(LambdaFunction<T, R> cf, Collection<?> values) {
        return in(cf, values, false);
    }

    public <T, R> LambdaConditions in(LambdaFunction<T, R> cf, Collection<?> values, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        in(generate(sl, prependClass), values);
        return this;
    }

    public <T, R> LambdaConditions like(LambdaFunction<T, R> cf, String value) {
        return like(cf, value, false);
    }

    public <T, R> LambdaConditions like(LambdaFunction<T, R> cf, String value, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        like(generate(sl, prependClass), value);
        return this;
    }

    public <T, R> LambdaConditions orderBy(LambdaFunction<T, R> cf) {
        return orderBy(cf, false);
    }

    public <T, R> LambdaConditions orderBy(LambdaFunction<T, R> cf, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        orderBy(generate(sl, prependClass));
        return this;
    }

    public <T, R> LambdaConditions orderByDesc(LambdaFunction<T, R> cf) {
        return orderByDesc(cf, false);
    }

    public <T, R> LambdaConditions orderByDesc(LambdaFunction<T, R> cf, boolean prependClass) {
        SerializedLambda sl = cf.getSerializedLambda();
        orderByDesc(generate(sl, prependClass));
        return this;
    }

    /**
     * 生成最终的 key
     *
     * @param prependClass 是否要拼接 ClassName
     */
    protected String generate(SerializedLambda sl, boolean prependClass) {
        if (prependClass) {
            return LambdaFunction.getClassName(sl).lowercaseFirst().getValue() +
                    LambdaFunction.getMethodName(sl).getValue();
        } else {
            return LambdaFunction.getMethodName(sl).lowercaseFirst().getValue();
        }
    }
}
