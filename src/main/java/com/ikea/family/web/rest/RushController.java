package com.ikea.family.web.rest;

import com.ikea.family.domain.service.DbLockService;
import com.ikea.family.domain.service.RedisLockService;
import com.ikea.family.domain.service.ZookeeperLockService;
import com.ikea.family.web.rest.vo.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guohang
 */
@RestController
@AllArgsConstructor
@RequestMapping("/rush")
public class RushController {

    private final DbLockService dbLockService;

    private final RedisLockService redisLockService;

    private final ZookeeperLockService zookeeperLockService;

    @PutMapping("/db-lock/unique")
    public Result<Void> dbTest(@RequestParam Integer userId,
                               @RequestParam Integer productId) {
        return dbLockService.dbUniqueLockRush(userId, productId);
    }

    @PutMapping("/db-lock/for-update")
    public Result<Void> dbForUpdateTest(@RequestParam Integer userId,
                                        @RequestParam Integer productId) {
        return dbLockService.dbForUpdateLockRush(userId, productId);
    }

    @PutMapping("/redis-lock/redisson")
    public Result<Void> redisRedissonTest(@RequestParam Integer userId,
                                          @RequestParam Integer productId) {
        return redisLockService.redissonLockRush(userId, productId);
    }

    @PutMapping("/redis-lock/lua")
    public Result<Void> redisLuaTest(@RequestParam Integer userId,
                                     @RequestParam Integer productId) {
        return redisLockService.redisLuaLockRush(userId, productId);
    }

    @PutMapping("/zk-lock")
    public Result<Void> zkTest(@RequestParam Integer userId,
                               @RequestParam Integer productId) {
        return zookeeperLockService.zookeeperLockRush(userId, productId);
    }

}
