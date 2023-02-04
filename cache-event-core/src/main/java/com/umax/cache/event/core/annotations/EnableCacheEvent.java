package com.umax.cache.event.core.annotations;

import com.umax.cache.event.core.registry.CacheEventRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangyingbo
 * @date 2023-01-30 17:37
 **/
@Import(CacheEventRegistry.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableCacheEvent {
}
