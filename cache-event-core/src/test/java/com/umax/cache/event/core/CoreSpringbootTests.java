package com.umax.cache.event.core;

import com.umax.cache.event.core.bean.CacheBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

/**
 * @author wangyingbo
 * @date 2023-02-02 9:51
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class CoreSpringbootTests {

    @Autowired
    CacheBean bean;

    @Test
    public void test() throws InterruptedException {
        Stream.of(bean.list()).forEach(System.out::println);
        Stream.of(bean.list()).forEach(System.out::println);
        Stream.of(bean.list1()).forEach(System.out::println);
        Stream.of(bean.list1()).forEach(System.out::println);
        Stream.of(bean.list2()).forEach(System.out::println);
        Stream.of(bean.list2()).forEach(System.out::println);
        bean.clear();
        // mq有延迟
        Thread.sleep(2000);
        Stream.of(bean.list()).forEach(System.out::println);
        Stream.of(bean.list1()).forEach(System.out::println);
        Stream.of(bean.list2()).forEach(System.out::println);
    }
}
