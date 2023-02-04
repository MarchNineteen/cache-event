package com.umax.cache.event.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * 缓存清除事件
 *
 * @author wangyingbo
 * @date 2023-01-30 17:40
 **/
public class CacheClearEvent<T> extends ApplicationEvent {

    private String eventName;


    public CacheClearEvent(Object source) {
        super(source);
    }

    public CacheClearEvent(Object source, String eventName) {
        super(source);
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

}
