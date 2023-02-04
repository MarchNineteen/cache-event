package com.umax.cache.event.rabbitmq.bean;

import com.umax.cache.event.common.annotations.EventCacheEvict;
import com.umax.cache.event.common.annotations.EventCacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangyingbo
 * @date 2023-01-30 18:41
 **/
@Service
public class CacheBean {

    @EventCacheEvict(eventNames = {"clear_dept_list", "clear_dept_list_1"}, value = {"dept_list_1"})
    public void clear() {

    }

//    @EventCacheEvict(eventNames = {"clear_dept_list_1",}, value = {"dept_list_1"})
//    public void clear1() {
//
//    }

    @EventCacheable(listenEventNames = {"clear_dept_list", "clear_dept_list_1"}, value = {"dept_list", "dept_list2"})
    public List<String> list() {
        System.out.print("非缓存操作");
        return List.of("1", "2");
    }

    @EventCacheable(listenEventNames = {"clear_dept_list"}, value = {"dept_by_id"}, key = "#id")
    public String getById(String id) {
        System.out.print("非缓存操作");
        return id;
    }

    //    @EventCacheable(cacheNames = "dept_list_1", listenEventNames = {"clear_dept_list"})
    @EventCacheable(listenEventNames = {"clear_dept_list_1"}, cacheNames = "dept_list_1")
    public List<String> list1() {
        System.out.print("非缓存操作");
        return List.of("3", "4");
    }

    @EventCacheable(listenEventNames = {"clear_dept_list_1"}, cacheNames = "dept_list_1")
    public List<String> list2() {
        System.out.print("非缓存操作");
        return List.of("5", "6");
    }
}
