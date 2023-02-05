package com.umax.cache.event.core;

import com.umax.cache.event.core.annotations.EnableCacheEvent;
import com.umax.cache.event.core.bean.CacheBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.stream.Stream;

/**
 * @author wangyingbo
 * @date 2023-01-30 18:39
 **/

@ComponentScan(basePackages = "com.umax.cache.event")
@EnableCacheEvent
@EnableAspectJAutoProxy
@EnableCaching
public class CoreSpringTests {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(CoreSpringTests.class);
        CacheBean bean = ctx.getBean(CacheBean.class);
        Stream.of(bean.list()).forEach(System.out::println);
        Stream.of(bean.list()).forEach(System.out::println);
        Stream.of(bean.list1()).forEach(System.out::println);
        Stream.of(bean.list1()).forEach(System.out::println);
        Stream.of(bean.getById("1")).forEach(System.out::println);
        Stream.of(bean.getById("1")).forEach(System.out::println);
        bean.clear();
        Stream.of(bean.list()).forEach(System.out::println);
        Stream.of(bean.list1()).forEach(System.out::println);
        Stream.of(bean.getById("1")).forEach(System.out::println);
        Stream.of(bean.getById("1")).forEach(System.out::println);
    }

}
