package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.domain.repository.QueryUserRepository;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.infrastructure.persistence.mapper.SysUserMapper;
import org.springframework.stereotype.Repository;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-09 16:49
 *
 */
@Repository
@RequiredArgsConstructor
public class QueryUserRepositoryImpl implements QueryUserRepository {


    private final SysUserMapper sysUserMapper;

    @Override
    public SysUserEntity findByUsername(String username) {
        LambdaQueryWrapper<SysUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserEntity::getUsername, username);
        SysUserEntity user = sysUserMapper.selectById(queryWrapper);
        return null;
    }
}
