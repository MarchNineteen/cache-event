package com.umax.cache.event.core.annotations;

import com.umax.cache.event.core.registry.WebCacheEventRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangyingbo
 * @date 2023-01-30 17:37
 **/
@Import(WebCacheEventRegistry.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableWebCacheEvent {
}
