package com.umax.cache.event.core.listen;

import com.umax.cache.event.common.annotations.EventCacheable;

import java.util.List;

/**
 * @author wangyingbo
 * @date 2023-02-02 17:13
 **/
public interface CacheClearEventListenerBuilder {

    public void buildListener(String eventName);

    void addEventCacheable(List<EventCacheable> eventCacheList);
}
