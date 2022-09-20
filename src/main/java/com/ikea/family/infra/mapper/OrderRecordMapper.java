package com.ikea.family.infra.mapper;

import com.ikea.family.infra.po.OrderRecord;
import com.ikea.family.infra.po.OrderRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderRecordMapper {
    long countByExample(OrderRecordExample example);

    int deleteByExample(OrderRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderRecord row);

    int insertSelective(OrderRecord row);

    List<OrderRecord> selectByExample(OrderRecordExample example);

    OrderRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") OrderRecord row, @Param("example") OrderRecordExample example);

    int updateByExample(@Param("row") OrderRecord row, @Param("example") OrderRecordExample example);

    int updateByPrimaryKeySelective(OrderRecord row);

    int updateByPrimaryKey(OrderRecord row);
}