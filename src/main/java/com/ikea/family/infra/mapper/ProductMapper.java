package com.ikea.family.infra.mapper;

import com.ikea.family.infra.po.Product;
import com.ikea.family.infra.po.ProductExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    long countByExample(ProductExample example);

    int deleteByExample(ProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Product row);

    int insertSelective(Product row);

    List<Product> selectByExample(ProductExample example);

    Product selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Product row, @Param("example") ProductExample example);

    int updateByExample(@Param("row") Product row, @Param("example") ProductExample example);

    int updateByPrimaryKeySelective(Product row);

    int updateByPrimaryKey(Product row);

    Product selectForUpdateById(@Param("productId") Integer productId);
}