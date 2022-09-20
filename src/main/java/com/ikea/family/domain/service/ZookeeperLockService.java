package com.ikea.family.domain.service;

import com.ikea.family.domain.repository.OrderRecordRepository;
import com.ikea.family.domain.repository.ProductRepository;
import com.ikea.family.infra.manager.ZookeeperManager;
import com.ikea.family.infra.po.OrderRecord;
import com.ikea.family.infra.po.Product;
import com.ikea.family.web.rest.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author guohang
 */
@Slf4j
@Service
@AllArgsConstructor
public class ZookeeperLockService {

    private final CuratorFramework client;

    private final ProductRepository productRepository;

    private final OrderRecordRepository orderRecordRepository;

    @Transactional
    public Result<Void> zookeeperLockRush(Integer userId, Integer productId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ZookeeperManager zkUserLock = null;
        ZookeeperManager zkProductLock = null;
        try {
            zkUserLock = new ZookeeperManager(client, methodName + ":user:" + userId);
            boolean lock = zkUserLock.tryLock(1000);
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
            zkProductLock = new ZookeeperManager(client, "product:" + productId);
            boolean productLock = zkProductLock.tryLock(1000);
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
            try {
                if (zkUserLock != null) {
                    zkUserLock.unLock();
                }
                if (zkProductLock != null) {
                    zkProductLock.unLock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.ok();
    }

}
