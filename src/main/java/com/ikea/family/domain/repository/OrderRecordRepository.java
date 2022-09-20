package com.ikea.family.domain.repository;

import com.ikea.family.infra.mapper.OrderRecordMapper;
import com.ikea.family.infra.po.OrderRecord;
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
public class OrderRecordRepository {

    private final OrderRecordMapper orderRecordMapper;

    public List<OrderRecord> selectAll() {
        return orderRecordMapper.selectByExample(null);
    }

    public OrderRecord selectById(Integer id) {
        return orderRecordMapper.selectByPrimaryKey(id);
    }

    public int insert(OrderRecord orderRecord) {
        orderRecord.setCreateTime(LocalDateTime.now());
        orderRecord.setPayment(orderRecord.getPrice() * orderRecord.getQuantity());
        return orderRecordMapper.insert(orderRecord);
    }
}
