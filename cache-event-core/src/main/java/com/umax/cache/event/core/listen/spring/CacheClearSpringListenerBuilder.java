package com.umax.cache.event.core.listen.spring;

import com.umax.cache.event.common.annotations.EventCacheable;
import com.umax.cache.event.core.enums.SpringEventPushType;
import com.umax.cache.event.core.listen.CacheClearEventListenerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建监听器
 *
 * @author wangyingbo
 * @date 2023-01-30 17:56
 **/
public class CacheClearSpringListenerBuilder implements CacheClearEventListenerBuilder {

    private GenericApplicationContext applicationContext;
    private SpringEventPushType pushType;
    private List<EventCacheable> eventCacheList = new ArrayList<>();

    /**
     * 这里选择手动去添加bean
     *
     * @throws BeansException
     */
    @Override
    public void buildListener(String eventName) {
//        ConstructorArgumentValues cargs = new ConstructorArgumentValues();
//        cargs.addIndexedArgumentValue(0, eventCacheList);
//        cargs.addIndexedArgumentValue(1, applicationContext.getBean(CacheManager.class));
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(CacheClearSpringListener.class);
        applicationContext.registerBeanDefinition(eventName, rootBeanDefinition);
        // 需要getBean后才是真实的创建实例
        CacheClearSpringListener listener = applicationContext.getBean(eventName, CacheClearSpringListener.class);
        listener.addEventCacheable(eventCacheList);
    }


    public CacheClearSpringListenerBuilder(GenericApplicationContext applicationContext, SpringEventPushType pushType) {
        this.applicationContext = applicationContext;
        this.pushType = pushType;
    }

    @Override
    public void addEventCacheable(List<EventCacheable> eventCacheList) {
        this.eventCacheList.addAll(eventCacheList);
    }

}
