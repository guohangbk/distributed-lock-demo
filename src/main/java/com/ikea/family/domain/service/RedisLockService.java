package com.ikea.family.domain.service;

import com.ikea.family.domain.repository.OrderRecordRepository;
import com.ikea.family.domain.repository.ProductRepository;
import com.ikea.family.infra.manager.RedisLuaLockManager;
import com.ikea.family.infra.manager.RedissonManager;
import com.ikea.family.infra.po.OrderRecord;
import com.ikea.family.infra.po.Product;
import com.ikea.family.web.rest.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author guohang
 */
@Slf4j
@Service
@AllArgsConstructor
public class RedisLockService {

    private final RedissonManager redissonManager;

    private final RedisLuaLockManager redisLuaLockManager;

    private final ProductRepository productRepository;

    private final OrderRecordRepository orderRecordRepository;

    @Transactional
    public Result<Void> redissonLockRush(Integer userId, Integer productId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        RLock userLock = redissonManager.getLock(methodName + ":user:" + userId);
        RLock productLock = null;
        try {
            if (! userLock.tryLock(500, TimeUnit.MILLISECONDS)) {
                return Result.fail("5001", "请勿频繁点击");
            }
            Product product = productRepository.selectById(productId);
            if (Objects.isNull(product)) {
                return Result.fail("5003", "商品不存在");
            }
            if (product.getStock() == 0) {
                return Result.fail("5004", "商品库存不足");
            }
            productLock = redissonManager.getLock("product:" + productId);
            if (! productLock.tryLock(500, TimeUnit.MILLISECONDS)) {
                return Result.fail("5002", "系统繁忙，请刷新后重试。");
            }
            orderRecordRepository.insert(OrderRecord.builder()
                    .productId(productId).userId(userId).description(methodName)
                    .price(product.getPrice()).quantity(1).build());
            product.setStock(product.getStock() - 1);
            productRepository.updateByEntity(product);
            log.info("redisson rush product. productId: {}, stock: 1", productId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("redissonLock rush error, e: " + e);
        } finally {
            userLock.unlock();
            if (productLock != null) {
                productLock.unlock();
            }
        }
        return Result.ok();
    }

    @Transactional
    public Result<Void> redisLuaLockRush(Integer userId, Integer productId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            boolean lock = redisLuaLockManager.lock(methodName + ":user:" + userId, 1000 * 10, 6, 200);
            if (! lock) {
                return Result.fail("5001", "请勿频繁点击");
            }
            Product product = productRepository.selectById(productId);
            if (Objects.isNull(product)) {
                return Result.fail("5003", "商品不存在");
            }
            if (product.getStock() == 0) {
                return Result.fail("5004", "商品库存不足");
            }
            boolean productLock = redisLuaLockManager.lock(  "product:" + productId, 1000 * 10, 6, 200);
            if (! productLock) {
                return Result.fail("5002", "系统繁忙，请刷新后重试。");
            }
            orderRecordRepository.insert(OrderRecord.builder()
                    .productId(productId).userId(userId).description(methodName)
                    .price(product.getPrice()).quantity(1).build());
            product.setStock(product.getStock() - 1);
            productRepository.updateByEntity(product);
            log.info("redis lua rush product. productId: {}, stock: 1", productId);
        } finally {
            redisLuaLockManager.releaseLock(methodName + ":user:" + userId);
            redisLuaLockManager.releaseLock(methodName + ":product:" + productId);
        }
        return Result.ok();
    }

}
