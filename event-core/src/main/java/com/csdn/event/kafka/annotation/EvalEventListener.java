package com.csdn.event.kafka.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EvalEventListener {
    /**
     * 指定监听的事件类型（默认根据方法参数推断）
     */
    Class<?>[] eventType() default {};
}