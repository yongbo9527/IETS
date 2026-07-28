package org.company.finance.auth.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.auth.domain.repository.QueryUserRepository;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserInfoEntity;
import org.company.finance.auth.infrastructure.persistence.mapper.SysUserInfoMapper;
import org.company.finance.auth.infrastructure.persistence.mapper.SysUserMapper;
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
    private final SysUserInfoMapper sysUserInfoMapper;

    @Override
    public SysUserEntity findByUsername(String username) {
        LambdaQueryWrapper<SysUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserEntity::getUsername, username);
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public SysUserEntity findById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUserInfoEntity findUserInfoByUserId(Long userId) {
        LambdaQueryWrapper<SysUserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfoEntity::getUserId, userId);
        return sysUserInfoMapper.selectOne(queryWrapper);
    }
}
