//package com.umax.cache.event.core.listen.rabbitmq;
//
//import com.umax.cache.event.core.constant.RabbitConstants;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.support.RootBeanDefinition;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.GenericApplicationContext;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author wangyingbo
// * @date 2023-02-01 19:21
// **/
//@Configuration
//public class CacheClearRabbitMqListenerBuilder {
//
//    public static final Logger log = LoggerFactory.getLogger(CacheClearRabbitMqListenerBuilder.class);
//
//    private final GenericApplicationContext applicationContext;
//    private final ConnectionFactory connectionFactory;
//
//    public CacheClearRabbitMqListenerBuilder(GenericApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//        this.connectionFactory = applicationContext.getBean(ConnectionFactory.class);
//    }
//
//    public void build(String eventName) {
//        // 生成服务端
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        DirectExchange exchange = new DirectExchange(RabbitConstants.EXCHANGE_PRE + eventName);
//        Queue queue = queue(RabbitConstants.QUEUE_PRE + eventName);
//        rabbitAdmin.declareQueue(queue);
//        rabbitAdmin.declareExchange(exchange);
//        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(RabbitConstants.QUEUE_KEY_PRE + eventName));
//        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RabbitAdmin.class);
//        rootBeanDefinition.setSource(rabbitAdmin);
//        applicationContext.registerBeanDefinition(queue.getName(), rootBeanDefinition);
//        addNewListener(queue.getName());
//    }
//
//    private void addNewListener(String queueName) {
//        SimpleMessageListenerContainer container = applicationContext.getBean(SimpleMessageListenerContainer.class);
//        container.addQueueNames(queueName);
//        log.info("往MQ中添加新的listener成功,队列名称[{}]", queueName);
//        long consumerCount = container.getActiveConsumerCount();
//        log.info("添加成功:现有队列监听者[{}]个", consumerCount);
//    }
//
//    /**
//     * 解决consumer乱码问题
//     */
////    @Bean
////    Jackson2JsonMessageConverter messageConverter() {
////        return new Jackson2JsonMessageConverter();
////    }
//    public Queue queue(String name) {
//        Map<String, Object> args = new HashMap<>();
//        // 是否持久化
//        boolean durable = true;
//        // 仅创建者可以使用的私有队列，断开后自动删除
//        boolean exclusive = false;
//        // 至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才会自动删除
//        boolean autoDelete = false;
//        return new Queue(name, durable, exclusive, autoDelete, args);
//    }
//
//}
