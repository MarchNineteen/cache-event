package com.umax.cache.event.core.aop;

import com.umax.cache.event.common.annotations.EventCacheEvict;
import com.umax.cache.event.core.properties.EventCacheProperties;
import com.umax.cache.event.core.push.EventCachePushService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * @author wangyingbo
 * @date 2023-01-30 19:47
 **/
@Aspect
@Component
public class EventCacheEvictAop implements ApplicationContextAware {

    public static final Logger log = LoggerFactory.getLogger(EventCacheEvictAop.class);

    @Autowired(required = false)
    private EventCachePushService pushService;

    @Autowired
    private EventCacheProperties eventCacheProperties;

    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.umax.cache.event.common.annotations.EventCacheEvict)")
    public void catchAll() {
    }

    // todo after是否合理

    /**
     * @param joinPoint
     * @return
     */
    @After("catchAll()")
    public void doAfter(JoinPoint joinPoint) {
        String[] eventNames = AnnotationUtils.getAnnotation(((MethodSignature) joinPoint.getSignature()).getMethod(), EventCacheEvict.class).eventNames();
        Stream.of(eventNames).forEach(eventName -> {
            log.info("事件[{}]发布,推送方式[{}]", eventName, eventCacheProperties.getPushType());
            switch (eventCacheProperties.getPushType()) {
                case SPRING_EVENT: {
                    Object bean = applicationContext.getBean(eventName);
                    applicationContext.publishEvent(bean);
                    break;
                }
                case RABBIT_MQ: {
                    // 发送mq
                    pushService.push(eventName);
                    break;
                }
                default: {
                    throw new RuntimeException("未设置默认推送方式");
                }
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
