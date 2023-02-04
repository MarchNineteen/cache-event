package com.umax.cache.event.rabbitmq.push;

import com.umax.cache.event.core.constant.RabbitConstants;
import com.umax.cache.event.core.push.EventCachePushService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangyingbo
 * @date 2023-02-03 16:45
 **/
@Component
public class RabbitmqPushServiceImpl implements EventCachePushService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void push(String eventName) {
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_PRE + eventName,
                RabbitConstants.QUEUE_KEY_PRE + eventName, eventName);
    }
}
