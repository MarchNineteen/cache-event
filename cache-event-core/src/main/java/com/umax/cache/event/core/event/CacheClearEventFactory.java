package com.umax.cache.event.core.event;

import com.umax.cache.event.common.annotations.EventCacheEvict;
import com.umax.cache.event.core.enums.SpringEventPushType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 缓存清除事件
 *
 * @author wangyingbo
 * @date 2023-01-30 17:40
 **/
public class CacheClearEventFactory {
    public static final Logger log = LoggerFactory.getLogger(CacheClearEventFactory.class);

    private GenericApplicationContext applicationContext;

    private SpringEventPushType pushType;
    public List<EventCacheEvict> evictList = new ArrayList<>();

    // 所有事件名称的集合
    public static Set<String> eventNameList = new HashSet<>();

    public void build() throws BeansException {
        evictList.forEach(evict ->
                Stream.of(evict.eventNames())
                        .distinct()
                        .filter(eventName -> !eventNameList.contains(eventName))
                        .forEach(eventName -> {
                            eventNameList.add(eventName);
                            log.info("register cacheClearEvent success, eventName:[{}]", eventName);
                            switch (pushType) {
                                case SPRING_EVENT: {
                                    ConstructorArgumentValues cargs = new ConstructorArgumentValues();
                                    cargs.addIndexedArgumentValue(0, evict.eventNames());
                                    cargs.addIndexedArgumentValue(1, eventName);
                                    RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(CacheClearEvent.class, cargs, null);
                                    applicationContext.registerBeanDefinition(eventName, rootBeanDefinition);
                                    break;
                                }
                                case RABBIT_MQ: {
                                    CacheClearEventBuilder cacheClearEventBuilder = applicationContext.getBean(CacheClearEventBuilder.class);
                                    if (null == cacheClearEventBuilder) {
                                        throw new RuntimeException("未配置RABBIT_MQ");
                                    }
                                    cacheClearEventBuilder.build(eventName);
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("未设置默认推送方式");
                                }
                            }
                        }));
    }


    public CacheClearEventFactory(GenericApplicationContext applicationContext, SpringEventPushType pushType) {
        this.applicationContext = applicationContext;
        this.pushType = pushType;
    }

    public boolean addEvict(EventCacheEvict evict) {
        return evictList.add(evict);
    }

    public boolean addEvict(List<EventCacheEvict> evictList) {
        return this.evictList.addAll(evictList);
    }
}
