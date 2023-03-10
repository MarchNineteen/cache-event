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
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = (GenericApplicationContext) applicationContext;
        pushType = eventCacheProperties.getPushType();
        eventFactory = new CacheClearEventFactory(this.applicationContext, pushType);
        springListenerBuilder = new CacheClearSpringListenerBuilder(this.applicationContext, pushType);
        // ???????????????????????????
        if (SpringEventPushType.SPRING_EVENT != pushType) {
            listenerBuilder = applicationContext.getBean(CacheClearEventListenerBuilder.class);
            if (null == listenerBuilder) {
                throw new RuntimeException("????????????????????????");
            }
            eventBuilder = applicationContext.getBean(CacheClearEventBuilder.class);
            if (null == eventBuilder) {
                throw new RuntimeException("????????????????????????builder");
            }
        }
    }

    protected void start() {
        findAllAnnotationWithSpring(applicationContext);
        // ??????tomcat???????????????????????????????????????????????????????????????????????????mvc?????????
//        findAllAnnotation(applicationContext);
        populateEventCacheList();
        buildEvent();
        buildListener();
    }

    private void findAllAnnotationWithSpring(ApplicationContext applicationContext) {
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(Component.class);
        beansWithAnnotationMap
                .values()
                .parallelStream()
                .forEach(bean -> processBean(AopUtils.getTargetClass(bean)));
    }

    private void findAllAnnotation(ApplicationContext applicationContext) {
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(Component.class);
        beansWithAnnotationMap
                .values()
                .parallelStream()
                .forEach(value -> {
            Class<?> clazz = AopUtils.getTargetClass(value);
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
        // rabbit_mq???builder?????????????????????????????? ?????????????????????
//            listener.addEventCacheable(eventCacheableList);
//        }
//        switch (pushType) {
//            case SPRING_EVENT:
//                springListenerBuilder.addEventCacheable(eventCacheableList);
//                break;
//            case RABBIT_MQ:
//                // ???builder?????????????????????????????? ?????????????????????
//                listener.addEventCacheable(eventCacheableList);
//                break;
//            default: {
//                throw new RuntimeException("???????????????????????????");
//            }
//        }
    }

    private void buildEvent() {
        eventFactory.build();
    }

    private void buildListener() {
        switch (pushType) {
            case SPRING_EVENT:
                // ??????????????????????????????
                springListenerBuilder.buildListener(pushType.name());
                break;
            case RABBIT_MQ:
                // ???????????? RabbitMqConfig ??????
                listenerBuilder.buildListener(pushType.name());
                break;
            default: {
                throw new RuntimeException("???????????????????????????");
            }
        }
    }

    private void processBean(final Class<?> targetType) {
        if (!this.nonAnnotatedClasses.contains(targetType) &&
                targetType.getName().startsWith("com.umax.")) {
            Map<Method, EventCacheable> cacheableMethods = MethodIntrospector.selectMethods(targetType,
                    (MethodIntrospector.MetadataLookup<EventCacheable>) method ->
                            AnnotatedElementUtils.findMergedAnnotation(method, EventCacheable.class));

            Map<Method, EventCacheEvict> evictMethods = MethodIntrospector.selectMethods(targetType,
                    (MethodIntrospector.MetadataLookup<EventCacheEvict>) method ->
                            AnnotatedElementUtils.findMergedAnnotation(method, EventCacheEvict.class));

            if (CollectionUtils.isEmpty(cacheableMethods) && CollectionUtils.isEmpty(evictMethods)) {
                this.nonAnnotatedClasses.add(targetType);
            } else {
                // Non-empty set of methods
                eventCacheableList.addAll(cacheableMethods.values());
                eventCacheEvictList.addAll(evictMethods.values());
            }
        }
    }

}
