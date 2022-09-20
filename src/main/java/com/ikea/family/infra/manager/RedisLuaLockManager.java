package com.ikea.family.infra.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * @author guohang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLuaLockManager {

    private final StringRedisTemplate stringRedisTemplate;

    private final ThreadLocal<String> lockFlag = new ThreadLocal<>();

    private static final String LOCK_LUA = "return redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2]) ";

    private static final String UNLOCK_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final RedisScript<String> LOCK_SCRIPT = new DefaultRedisScript<>(LOCK_LUA, String.class);

    private static final RedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(UNLOCK_LUA, Long.class);

    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedis(key, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                log.info("lock failed, retrying..." + retryTimes + "," + key);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.error("lua lock error, e: " + e);
                Thread.currentThread().interrupt();
                return false;
            }
            result = setRedis(key, expire);
        }
        return result;
    }

    private boolean setRedis(String key, long expire) {
        String uuid = UUID.randomUUID().toString();
        lockFlag.set(uuid);
        String execute = stringRedisTemplate.execute(
                LOCK_SCRIPT,
                stringRedisTemplate.getStringSerializer(),
                stringRedisTemplate.getStringSerializer(),
                Collections.singletonList(key),
                uuid,
                String.valueOf(expire));

        return "OK".equals(execute);
    }

    public boolean releaseLock(String key) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long execute = stringRedisTemplate.execute(
                    UNLOCK_SCRIPT,
                    stringRedisTemplate.getStringSerializer(),
                    (RedisSerializer<Long>) stringRedisTemplate.getKeySerializer(),
                    Collections.singletonList(key),
                    lockFlag.get());

            log.info("release lock , key:{}, value:{}", key, lockFlag.get());
            return Objects.nonNull(execute) && execute > 0;
        } catch (Exception e) {
            log.error("release lock occurred an exception", e);
        } finally {
            lockFlag.remove();
        }
        return false;
    }

}
