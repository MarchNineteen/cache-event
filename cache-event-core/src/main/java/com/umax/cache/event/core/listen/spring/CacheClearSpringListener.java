package com.umax.cache.event.core.listen.spring;

import com.umax.cache.event.core.enums.SpringEventPushType;
import com.umax.cache.event.core.event.CacheClearEvent;
import com.umax.cache.event.core.listen.AbstractCacheClearEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * @author wangyingbo
 * @date 2023-01-30 17:56
 **/
public class CacheClearSpringListener extends AbstractCacheClearEventListener implements ApplicationListener<CacheClearEvent> {

    public static final Logger log = LoggerFactory.getLogger(CacheClearSpringListener.class);

//    public CacheClearSpringListener(List<EventCacheable> eventCacheList, CacheManager cacheManager) {
//        this.cacheManager = cacheManager;
//        eventCacheList.forEach(eventCacheable -> {
//            Stream.of(eventCacheable.listenEventNames()).forEach(eventName -> {
//                eventNameCacheNamesMap.computeIfAbsent(eventName, key -> new HashSet<>()).addAll(List.of(eventCacheable.cacheNames()));
//            });
//        });
//    }

    @Override
    public void onApplicationEvent(CacheClearEvent event) {
        log.info("[{}]接收消息,准备清理事件:[{}]的缓存", SpringEventPushType.SPRING_EVENT, event.getEventName());
        process(event.getEventName());
    }

}
