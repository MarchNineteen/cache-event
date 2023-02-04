package com.umax.cache.event.core.event;

/**
 * @author wangyingbo
 * @date 2023-02-03 17:58
 **/
public interface CacheClearEventBuilder {

    public void build(String eventName);

//    void addEventCacheable(List<EventCacheable> eventCacheList);
}
