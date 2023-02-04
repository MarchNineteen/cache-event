package com.umax.cache.event.core.properties;

import com.umax.cache.event.core.enums.SpringEventPushType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: Marcher丶
 * @Date: 2022-07-12 14:09
 **/
@ConfigurationProperties(prefix = EventCacheProperties.PREFIX)
@Component
public class EventCacheProperties {

    public static final String PREFIX = "com.umax.cache-event";

    private SpringEventPushType pushType = SpringEventPushType.SPRING_EVENT;

    public void setPushType(SpringEventPushType pushType) {
        this.pushType = pushType;
    }


    public SpringEventPushType getPushType() {
        return pushType;
    }
}
