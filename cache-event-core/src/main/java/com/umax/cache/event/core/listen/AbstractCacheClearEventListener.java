package com.umax.cache.event.core.listen;

import com.umax.cache.event.common.annotations.EventCacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author wangyingbo
 * @date 2023-02-02 17:20
 **/
public abstract class AbstractCacheClearEventListener implements CacheClearEventListener {

    public static final Logger log = LoggerFactory.getLogger(AbstractCacheClearEventListener.class);

    @Autowired
    protected CacheManager cacheManager;

    protected Map<String, Set<String>> eventNameCacheNamesMap = new HashMap<>();

    @Override
    public void addEventCacheable(List<EventCacheable> eventCacheList) {
        eventCacheList.forEach(eventCacheable -> {
            Stream.of(eventCacheable.listenEventNames()).forEach(eventName -> {
                eventNameCacheNamesMap.computeIfAbsent(eventName, key -> new HashSet<>())
                        .addAll(List.of(eventCacheable.cacheNames()));
            });
        });
    }


    @Override
    public void clear(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (Objects.nonNull(cache)) {
            cache.clear();
        }
    }

    @Override
    public void evict(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (Objects.nonNull(cache)) {
            cache.evict(key);
        }
    }

    public void process(String eventName) {
        if (eventNameCacheNamesMap.containsKey(eventName)) {
            log.info("事件:[{}]在系统中已注册", eventName);
            processByCacheName(eventName);
        } else {
            log.info("事件:[{}]在系统中未注册", eventName);
        }
    }

//    private void processByCacheNameAndKey(String eventName) {
//        List<EventCacheable> eventCacheableList = eventNameCacheNamesMap.get(eventName);
//        List<EventCacheable> deleteByKey = eventCacheableList.parallelStream()
//                .filter(cache -> Objects.nonNull(cache.key()) && !StringUtils.isEmpty(cache.key()))
//                .collect(Collectors.toList());
//
//        List<EventCacheable> deleteByName = new ArrayList<>(eventCacheableList);
//        deleteByName.removeAll(deleteByKey);
//
//        deleteByKey.parallelStream()
//                .forEach(eventCacheable -> {
//                    EventCacheable annotation = AnnotationUtils.getAnnotation(eventCacheable, EventCacheable.class);
//                    evict(eventName, annotation.key());
//                    log.info("MQ清除事件:[{}]的缓存[{}]", eventName, annotation.key());
//                });
//        deleteByName.parallelStream().forEach(eventCacheable -> clear(eventName));
//    }

    private void processByCacheName(String eventName) {
        Set<String> cacheNames = eventNameCacheNamesMap.get(eventName);
        cacheNames.parallelStream().forEach(cacheName -> {
            log.info("清除事件:[{}]的缓存", cacheName);
            clear(cacheName);
        });
    }
}
