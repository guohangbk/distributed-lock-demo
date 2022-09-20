package com.ikea.family.domain.service;

import com.ikea.family.domain.repository.OrderRecordRepository;
import com.ikea.family.domain.repository.ProductRepository;
import com.ikea.family.domain.repository.ResourceLockRepository;
import com.ikea.family.infra.po.OrderRecord;
import com.ikea.family.infra.po.Product;
import com.ikea.family.web.rest.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author guohang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DbLockService {

    private final ResourceLockRepository resourceLockRepository;

    private final ProductRepository productRepository;

    private final OrderRecordRepository orderRecordRepository;

    /**
     * 数据库唯一索引实现分布式锁
     * 专门存储锁信息。当需要锁的时候，就增加一条数据，解锁就将这条数据删除即可。
     * 在数据中，锁名称是加了唯一索引的，限制加锁不会出现重复
     */
    @Transactional
    public Result<Void> dbUniqueLockRush(Integer userId, Integer productId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            boolean userLock = resourceLockRepository.lock(methodName + ":user:" + userId);
            if (! userLock) {
                return Result.fail("5001", "请勿频繁点击");
            }
            Product product = productRepository.selectById(productId);
            if (Objects.isNull(product)) {
                return Result.fail("5003", "商品不存在");
            }
            if (product.getStock() == 0) {
                return Result.fail("5004", "商品库存不足");
            }
            boolean lock = resourceLockRepository.lock("product:" + productId);
            if (! lock) {
                return Result.fail("5002", "系统繁忙，请刷新后重试。");
            }
            orderRecordRepository.insert(OrderRecord.builder()
                    .productId(productId).userId(userId).description(methodName)
                    .price(product.getPrice()).quantity(1).build());
            product.setStock(product.getStock() - 1);
            productRepository.updateByEntity(product);
            log.info("dbLock rush product. productId: {}, stock: 1", productId);
        } finally {
            resourceLockRepository.unlock(methodName + ":user:" + userId);
            resourceLockRepository.unlock(methodName + ":product:" + productId);
        }
        return Result.ok();
    }

    /**
     * 数据库排他锁实现分布式锁
     */
    @Transactional
    public Result<Void> dbForUpdateLockRush(Integer userId, Integer productId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            boolean userLock = resourceLockRepository.lock(methodName + ":user:" + userId);
            if (! userLock) {
                return Result.fail("5001", "请勿频繁点击");
            }
            Product product = productRepository.selectById(productId);
            if (Objects.isNull(product)) {
                return Result.fail("5003", "商品不存在");
            }
            product = productRepository.selectForUpdateById(productId);
            if (product.getStock() == 0) {
                return Result.fail("5004", "商品库存不足");
            }
            orderRecordRepository.insert(OrderRecord.builder()
                    .productId(productId).userId(userId).description(methodName)
                    .price(product.getPrice()).quantity(1).build());
            product.setStock(product.getStock() - 1);
            productRepository.updateByEntity(product);
            log.info("dbLock rush product. productId: {}, stock: 1", productId);
        } finally {
            resourceLockRepository.unlock(methodName + ":user:" + userId);
        }
        return Result.ok();
    }

}
