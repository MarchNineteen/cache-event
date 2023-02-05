package com.umax.cache.event.core.registry;

import com.umax.cache.event.common.annotations.EventCacheEvict;
import com.umax.cache.event.common.annotations.EventCacheable;
import com.umax.cache.event.core.enums.SpringEventPushType;
import com.umax.cache.event.core.event.CacheClearEventBuilder;
import com.umax.cache.event.core.event.CacheClearEventFactory;
import com.umax.cache.event.core.listen.CacheClearEventListenerBuilder;
import com.umax.cache.event.core.listen.spring.CacheClearSpringListenerBuilder;
import com.umax.cache.event.core.properties.EventCacheProperties;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangyingbo
 * @date 2023-02-03 22:08
 **/
public class EventRegistry implements ApplicationContextAware {
    private GenericApplicationContext applicationContext;
    @Autowired
    private EventCacheProperties eventCacheProperties;
    private SpringEventPushType pushType;

    private CacheClearSpringListenerBuilder springListenerBuilder;
    private CacheClearEventListenerBuilder listenerBuilder;
    private CacheClearEventBuilder eventBuilder;
    private CacheClearEventFactory eventFactory;

    private List<EventCacheable> eventCacheableList = new ArrayList<>();
    private List<EventCacheEvict> eventCacheEvictList = new ArrayList<>();

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = (GenericApplicationContext) applicationContext;
        pushType = eventCacheProperties.getPushType();
        eventFactory = new CacheClearEventFactory(this.applicationContext, pushType);
        springListenerBuilder = new CacheClearSpringListenerBuilder(this.applicationContext, pushType);
        // 初始化其他推送类型
        if (SpringEventPushType.SPRING_EVENT != pushType) {
            listenerBuilder = applicationContext.getBean(CacheClearEventListenerBuilder.class);
            if (null == listenerBuilder) {
                throw new RuntimeException("未实现该推送方式");
            }
            eventBuilder = applicationContext.getBean(CacheClearEventBuilder.class);
            if (null == eventBuilder) {
                throw new RuntimeException("未实现该推送方式builder");
            }
        }
    }

    protected void start() {
        // 对于tomcat工程来说，一般会加载两个上下文容器一个父容器，一个mvc子容器
        findAllAnnotation(applicationContext);
        populateEventCacheList();
        buildEvent();
        buildListener();
    }

    private void findAllAnnotation(ApplicationContext applicationContext) {
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(Component.class);
        beansWithAnnotationMap.values().forEach(value -> {
            Class clazz = AopUtils.getTargetClass(value);
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            for (Method method : methods) {
                if (method.isAnnotationPresent(EventCacheable.class)) {
                    EventCacheable eventCacheable = AnnotationUtils.getAnnotation(method, EventCacheable.class);
                    eventCacheableList.add(eventCacheable);
                }
                if (method.isAnnotationPresent(EventCacheEvict.class)) {
                    EventCacheEvict evict = AnnotationUtils.getAnnotation(method, EventCacheEvict.class);
                    eventCacheEvictList.add(evict);
                }
            }
        });
    }

    private void populateEventCacheList() {
        eventFactory.addEvict(eventCacheEvictList);
        springListenerBuilder.addEventCacheable(eventCacheableList);
        if (null != listenerBuilder) {
            listenerBuilder.addEventCacheable(eventCacheableList);
        }
//        if (null != eventBuilder) {
//            eventBuilder.addEventCacheable(eventCacheableList);
//        }
//        if (EventPushType.SPRING_EVENT == pushType) {
//            springListenerBuilder.addEventCacheable(eventCacheableList);
//        } else {
        // rabbit_mq在builder中导入会产生循坏依赖 所以在这里导入
//            listener.addEventCacheable(eventCacheableList);
//        }
//        switch (pushType) {
//            case SPRING_EVENT:
//                springListenerBuilder.addEventCacheable(eventCacheableList);
//                break;
//            case RABBIT_MQ:
//                // 在builder中导入会产生循坏依赖 所以在这里导入
//                listener.addEventCacheable(eventCacheableList);
//                break;
//            default: {
//                throw new RuntimeException("未设置默认推送方式");
//            }
//        }
    }

    private void buildEvent() {
        eventFactory.build();
    }

    private void buildListener() {
        switch (pushType) {
            case SPRING_EVENT:
                // 只需要创建一个监听器
                springListenerBuilder.buildListener(pushType.name());
                break;
            case RABBIT_MQ:
                // 监听器由 RabbitMqConfig 创建
                listenerBuilder.buildListener(pushType.name());
                break;
            default: {
                throw new RuntimeException("未设置默认推送方式");
            }
        }
    }

}
