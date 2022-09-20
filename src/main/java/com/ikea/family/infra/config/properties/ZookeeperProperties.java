package com.ikea.family.infra.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author guohang
 */
@Data
@ConfigurationProperties(prefix = "zookeeper.curator")
public class ZookeeperProperties {

    private String host;

    /**
     * 重试机制重试次数
     */
    private Integer maxRetries;

    /**
     * 会话超时时间
     */
    private Integer sessionTimeOut;

    /**
     * 连接超时时间
     */
    private Integer connectionTimeout;

    /**
     * 重试机制时间参数
     */
    private Integer sleepMsBetweenRetry;

}
