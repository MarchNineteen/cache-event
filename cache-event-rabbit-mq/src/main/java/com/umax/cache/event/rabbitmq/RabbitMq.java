package com.umax.cache.event.rabbitmq;

import com.umax.cache.event.core.annotations.EnableWebCacheEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author wangyingbo
 * @date 2023-02-02 9:51
 **/
@ComponentScan(basePackages = "com.umax.cache.event")
@EnableWebCacheEvent
@EnableAspectJAutoProxy
@EnableCaching
@SpringBootApplication
public class RabbitMq {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMq.class, args);
    }

}
