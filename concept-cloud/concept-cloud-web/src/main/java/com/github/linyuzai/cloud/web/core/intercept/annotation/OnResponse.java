package com.github.linyuzai.cloud.web.core.intercept.annotation;

import java.lang.annotation.*;

/**
 * 标记在拦截器类上表示拦截器的响应作用域
 * <p>
 * 标记在普通类的方法上在响应拦截时会调用该方法进行拦截
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnResponse {

}
