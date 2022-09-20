package com.ikea.family.domain.repository;

import com.ikea.family.infra.mapper.ProductMapper;
import com.ikea.family.infra.po.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author guohang
 */
@Slf4j
@Repository
@AllArgsConstructor
public class ProductRepository {

    private final ProductMapper productMapper;

    public List<Product> selectAll() {
        return productMapper.selectByExample(null);
    }

    public Product selectById(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    public int updateByEntity(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        return productMapper.updateByPrimaryKey(product);
    }

    public Product selectForUpdateById(Integer productId) {
        return productMapper.selectForUpdateById(productId);
    }
}
