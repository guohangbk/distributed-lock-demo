package com.ikea.family.infra.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guohang
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create();
    }

}
