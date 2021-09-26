package com.example.catl.hulibarry.sqlite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tanqimin on 2015/10/28.
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Table {
    /**
     * 数据表名称
     *
     * @return
     */
    String value() default "";
}
