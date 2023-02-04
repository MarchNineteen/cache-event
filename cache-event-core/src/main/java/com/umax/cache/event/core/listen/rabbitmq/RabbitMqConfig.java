//package com.umax.cache.event.core.listen.rabbitmq;
//
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author wangyingbo
// * @date 2023-02-02 15:04
// **/
//@Configuration
//public class RabbitMqConfig {
//
//    private final ConnectionFactory connectionFactory;
//    private final CacheClearRabbitMqListener rabbitMqListener;
//
//    public RabbitMqConfig(ConnectionFactory connectionFactory, CacheClearRabbitMqListener rabbitMqListener) {
//        this.connectionFactory = connectionFactory;
//        this.rabbitMqListener = rabbitMqListener;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer mqMessageContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
////        Set<String> list = CacheClearEventBuilder.eventNameList;
//        container.setConnectionFactory(connectionFactory);
//        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        container.setMaxConcurrentConsumers(10);
//        container.setConcurrentConsumers(5);
//        container.setPrefetchCount(1);
//        container.setMessageListener(rabbitMqListener);//监听处理类
//        return container;
//    }
//
//    // 先不开启
////    @Bean
////    public void start() {
////        mqMessageContainer().start();
////    }
//}
