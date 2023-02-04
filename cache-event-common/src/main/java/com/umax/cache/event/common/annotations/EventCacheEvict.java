package com.umax.cache.event.common.annotations;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author wangyingbo
 * @date 2023-01-30 17:04
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CacheEvict
public @interface EventCacheEvict {

    // 事件名称
    String[] eventNames() default {};

    // todo 清除要不要兼容  若兼容的话 value或者cacheNames要必填
     /******* @CacheEvict的注解 *******/
    @AliasFor(annotation = CacheEvict.class)
    String[] value() default {};

    @AliasFor(annotation = CacheEvict.class)
    String[] cacheNames() default {};

    @AliasFor(annotation = CacheEvict.class)
    String key() default "";

    @AliasFor(annotation = CacheEvict.class)
    String keyGenerator() default "";

    @AliasFor(annotation = CacheEvict.class)
    String cacheManager() default "";

    @AliasFor(annotation = CacheEvict.class)
    String cacheResolver() default "";

    @AliasFor(annotation = CacheEvict.class)
    String condition() default "";

    @AliasFor(annotation = CacheEvict.class)
    boolean allEntries() default false;

    @AliasFor(annotation = CacheEvict.class)
    boolean beforeInvocation() default false;
}
