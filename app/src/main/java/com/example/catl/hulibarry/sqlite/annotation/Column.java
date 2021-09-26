package com.example.catl.hulibarry.sqlite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tanqimin on 2015/10/28.
 */
@java.lang.annotation.Inherited
@java.lang.annotation.Target({
        ElementType.FIELD
})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Column {
    /**
     * 列名
     *
     * @return
     */
    String value() default "";

    /**
     * 是否只读？只读字段不参与 INSERT 和 UPDATE
     *
     * @return
     */
    boolean readOnly() default false;

    /**
     * 是否主键？
     *
     * @return
     */
    boolean isPrimaryKey() default false;
}
