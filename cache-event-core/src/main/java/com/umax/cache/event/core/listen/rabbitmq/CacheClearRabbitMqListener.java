//package com.umax.cache.event.core.listen.rabbitmq;
//
//import com.rabbitmq.client.Channel;
//import com.umax.cache.event.core.enums.EventPushType;
//import com.umax.cache.event.core.listen.AbstractCacheClearEventListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.stereotype.Component;
//
//import java.io.UnsupportedEncodingException;
//
///**
// * @author wangyingbo
// * @date 2023-01-30 17:56
// **/
//@Component
//public class CacheClearRabbitMqListener extends AbstractCacheClearEventListener implements ChannelAwareMessageListener {
//
//    public static final Logger log = LoggerFactory.getLogger(CacheClearRabbitMqListener.class);
//
//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//        String eventName = "";
//        try {
//            eventName = new String(message.getBody(), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        log.info("[{}]接收消息,准备清理事件:[{}]的缓存", EventPushType.RABBIT_MQ, eventName);
//        process(eventName);
//    }
//}
