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
public class Product {
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}