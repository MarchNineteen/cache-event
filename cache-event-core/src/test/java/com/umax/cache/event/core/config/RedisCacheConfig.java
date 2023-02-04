package com.umax.cache.event.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RedisCacheConfig {

    @Value("${spring.cache.type:caffeine}")
    private String cacheType;

    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static Integer DEFAULT_EXPIRE_TIME = 60 * 10;

    @Bean
    public CacheManager cacheManager() {
        return caffeineCacheManager();
    }


    /**
     * caffeine 不支持不同key设置不同的过期时间
     */
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                        // 设置最后一次写入或访问后经过固定时间过期
                        .expireAfterWrite(DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS)
                        // 初始的缓存空间大小
                        .initialCapacity(100)
                // 缓存的最大条数
//                .maximumSize(1000)
        );
        return cacheManager;
    }

}
