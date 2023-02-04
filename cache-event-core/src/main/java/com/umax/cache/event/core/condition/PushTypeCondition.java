package com.umax.cache.event.core.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author wangyingbo
 * @date 2023-02-03 16:50
 **/
public class PushTypeCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (context.getBeanFactory().containsBean("eventCacheProperties")) {
			return true;
		}
		return false;

//        BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition("eventCacheProperties");
//        if (null != beanDefinition && SpringEventPushType.SPRING_EVENT.name() == beanDefinition.getBeanClassName()) {
//            return true;
//        }
//        return false;
    }
}
