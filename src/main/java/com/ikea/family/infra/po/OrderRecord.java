package com.ikea.family.infra.po;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecord {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Double price;

    private Integer quantity;

    private Double payment;

    private LocalDateTime createTime;

    private String description;
}