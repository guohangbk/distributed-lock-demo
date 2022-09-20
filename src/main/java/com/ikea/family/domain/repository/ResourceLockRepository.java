package com.ikea.family.domain.repository;

import com.ikea.family.infra.mapper.ResourceLockMapper;
import com.ikea.family.infra.po.ResourceLock;
import com.ikea.family.infra.po.ResourceLockExample;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author guohang
 */
@Slf4j
@Repository
@AllArgsConstructor
public class ResourceLockRepository {

    private final ResourceLockMapper resourceLockMapper;

    public List<ResourceLock> selectAll() {
        return resourceLockMapper.selectByExample(null);
    }

    public boolean lock(String lockName) {
        ResourceLockExample example = new ResourceLockExample();
        example.createCriteria().andLockNameEqualTo(lockName);
        List<ResourceLock> resourceLocks = resourceLockMapper.selectByExample(example);
        if (! CollectionUtils.isEmpty(resourceLocks)) {
            return false;
        }
        int i = resourceLockMapper.insert(ResourceLock.builder()
                .lockName(lockName).createTime(LocalDateTime.now()).build());
        log.info("lock method: {}", lockName);
        return i == 1;
    }

    public boolean unlock(String lockName) {
        ResourceLockExample example = new ResourceLockExample();
        example.createCriteria().andLockNameEqualTo(lockName);
        List<ResourceLock> resourceLocks = resourceLockMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resourceLocks)) {
            return false;
        }
        int i = resourceLockMapper.deleteByPrimaryKey(resourceLocks.get(0).getId());
        log.info("unlock method: {}", lockName);
        return i == 1;
    }

}
