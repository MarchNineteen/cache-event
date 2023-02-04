package com.umax.cache.event.core.registry;

import org.springframework.boot.CommandLineRunner;

/**
 * @author wangyingbo
 * @date 2023-01-30 17:13
 **/
public class WebCacheEventRegistry extends EventRegistry implements CommandLineRunner {

    @Override
    public void run(String... args) {
        start();
    }

}