package com.ikea.family.infra.config;

import com.ikea.family.infra.config.properties.ZookeeperProperties;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guohang
 */
@EnableConfigurationProperties(ZookeeperProperties.class)
@Configuration
@RequiredArgsConstructor
public class ZookeeperConfig {

    private final ZookeeperProperties zookeeperProperties;

    /**
     * - `session`重连策略
     - `RetryPolicy retry Policy = new RetryOneTime(3000);`
     - 说明：三秒后重连一次，只重连一次
     - `RetryPolicy retryPolicy = new RetryNTimes(3,3000);`
     - 说明：每三秒重连一次，重连三次
     - `RetryPolicy retryPolicy = new RetryUntilElapsed(1000,3000);`
     - 说明：每三秒重连一次，总等待时间超过个`10`秒后停止重连
     - `RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3)`
     - 说明：这个策略的重试间隔会越来越长
     - 公式：`baseSleepTImeMs * Math.max(1,random.nextInt(1 << (retryCount + 1)))`
     - `baseSleepTimeMs` = `1000` 例子中的值
     - `maxRetries` = `3` 例子中的值
     */
    @Bean
    public CuratorFramework curatorClient() {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //连接地址  集群用,隔开
                .connectString(zookeeperProperties.getHost())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeout())
                //会话超时时间
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeOut())
                //设置重试机制
                .retryPolicy(
                        new ExponentialBackoffRetry(
                                zookeeperProperties.getSleepMsBetweenRetry(),
                                zookeeperProperties.getMaxRetries()))
                .build();
        client.start();
        return client;
    }

}
