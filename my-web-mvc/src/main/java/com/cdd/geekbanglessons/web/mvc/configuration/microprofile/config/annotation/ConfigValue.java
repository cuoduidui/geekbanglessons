package com.cdd.geekbanglessons.web.mvc.configuration.microprofile.config.annotation;

import java.lang.annotation.*;

/**
 * 配置变量
 *
 * @author yangfengshan
 * @date 2021-03-17 10:53
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigValue {
    String value() default "";
}
