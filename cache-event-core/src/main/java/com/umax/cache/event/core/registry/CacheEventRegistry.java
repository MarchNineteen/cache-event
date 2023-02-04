package com.umax.cache.event.core.registry;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author wangyingbo
 * @date 2023-01-30 17:13
 **/
public class CacheEventRegistry extends EventRegistry implements ApplicationListener<ContextRefreshedEvent> {

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        start();
    }
}