package com.ikea.family.infra.manager;

import lombok.AllArgsConstructor;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

/**
 * @author guohang
 */
@Component
@AllArgsConstructor
public class RedissonManager {

    private final RedissonClient redissonClient;

    /**
     * 获取字符串对象
     */
    public <T> RBucket<T> getBucket(String objectName) {
        return redissonClient.getBucket(objectName);
    }

    /**
     * 获取Map对象
     */
    public <K, V> RMap<K, V> getMap(String objectName) {
        return redissonClient.getMap(objectName);
    }

    /**
     * 获取有序集合
     */
    public <V> RSortedSet<V> getSortedSet(String objectName) {
        return redissonClient.getSortedSet(objectName);
    }

    /**
     * 获取集合
     */
    public <V> RSet<V> getSet(String objectName) {
        return redissonClient.getSet(objectName);
    }

    /**
     * 获取列表
     */
    public <V> RList<V> getList(String objectName) {
        return redissonClient.getList(objectName);
    }

    /**
     * 获取队列
     */
    public <V> RQueue<V> getQueue(String objectName) {
        return redissonClient.getQueue(objectName);
    }

    /**
     * 获取双端队列
     */
    public <V> RDeque<V> getDeque(String objectName) {
        return redissonClient.getDeque(objectName);
    }


    /**
     * 获取锁
     */
    public RLock getLock(String objectName) {
        return redissonClient.getLock(objectName);
    }

    /**
     * 获取读取锁
     */
    public RReadWriteLock getReadWriteLock(String objectName) {
        return redissonClient.getReadWriteLock(objectName);
    }

    /**
     * 获取原子数
     */
    public RAtomicLong getAtomicLong(String objectName) {
        return redissonClient.getAtomicLong(objectName);
    }

    /**
     * 获取记数锁
     */
    public RCountDownLatch getCountDownLatch(String objectName) {
        return redissonClient.getCountDownLatch(objectName);
    }

}
