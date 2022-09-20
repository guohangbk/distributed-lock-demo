package com.ikea.family.infra.mapper;

import com.ikea.family.infra.po.ResourceLock;
import com.ikea.family.infra.po.ResourceLockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ResourceLockMapper {
    long countByExample(ResourceLockExample example);

    int deleteByExample(ResourceLockExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ResourceLock row);

    int insertSelective(ResourceLock row);

    List<ResourceLock> selectByExample(ResourceLockExample example);

    ResourceLock selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") ResourceLock row, @Param("example") ResourceLockExample example);

    int updateByExample(@Param("row") ResourceLock row, @Param("example") ResourceLockExample example);

    int updateByPrimaryKeySelective(ResourceLock row);

    int updateByPrimaryKey(ResourceLock row);
}