package com.ikea.family.infra.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @author guohang
 */
@Slf4j
public class ZookeeperManager {

    private final InterProcessMutex lock;

    private final CuratorFramework client;

    public ZookeeperManager(CuratorFramework client, String lockKey) {
        String path = "/rush/" + lockKey;
        this.client = client;
        this.lock = new InterProcessMutex(client, path);
    }

    public void lock() {
        try {
            lock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean tryLock() {
        return tryLock(0);
    }

    public boolean tryLock(long timeout) {
        try {
            return lock.acquire(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unLock() throws Exception {
        lock.release();
    }

    public void shutdown() {
        client.close();
    }

}
