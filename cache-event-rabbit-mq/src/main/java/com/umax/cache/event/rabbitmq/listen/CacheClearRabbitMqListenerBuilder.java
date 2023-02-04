package com.umax.cache.event.rabbitmq.listen;

import com.umax.cache.event.common.annotations.EventCacheable;
import com.umax.cache.event.core.listen.CacheClearEventListenerBuilder;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wangyingbo
 * @date 2023-02-02 15:04
 **/
@Configuration
public class CacheClearRabbitMqListenerBuilder implements CacheClearEventListenerBuilder {

    private final ConnectionFactory connectionFactory;
    private final CacheClearRabbitMqListener listener;

    public CacheClearRabbitMqListenerBuilder(ConnectionFactory connectionFactory, CacheClearRabbitMqListener listener) {
        this.connectionFactory = connectionFactory;
        this.listener = listener;
    }

    @Bean
    public SimpleMessageListenerContainer mqMessageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setMaxConcurrentConsumers(10);
        container.setConcurrentConsumers(5);
        container.setPrefetchCount(1);
        //监听处理类
        container.setMessageListener(listener);
        return container;
    }

    @Override
    public void buildListener(String eventName) {
        //
    }

    @Override
    public void addEventCacheable(List<EventCacheable> eventCacheList) {
        listener.addEventCacheable(eventCacheList);
    }

    // 先不开启
//    @Bean
//    public void start() {
//        mqMessageContainer().start();
//    }

}
