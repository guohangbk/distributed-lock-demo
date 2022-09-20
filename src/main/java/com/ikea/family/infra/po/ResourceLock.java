package com.ikea.family.infra.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceLock {
    private Integer id;

    private String lockName;

    private String description;

    private LocalDateTime createTime;
}