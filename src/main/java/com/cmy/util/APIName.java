package com.cmy.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：获取API的名称
 *
 * @author 陈淼英
 * @create 2021/5/31 0031 10:19
 */

@Retention(RetentionPolicy.RUNTIME)


@Target(value = {ElementType.METHOD,ElementType.TYPE})
public @interface APIName {
    //注解成员，default表示默认值
    public String value() default "";

}
