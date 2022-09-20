package com.ikea.family;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author guohang
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.ikea.family.infra.mapper"})
public class DistributedLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockApplication.class,args);
    }
}
