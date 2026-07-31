package org.company.finance.auth.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.company.finance.auth.domain.repository.CommandUserRepository;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserInfoEntity;
import org.company.finance.auth.infrastructure.persistence.mapper.SysUserInfoMapper;
import org.company.finance.auth.infrastructure.persistence.mapper.SysUserMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommandUserRepositoryImpl implements CommandUserRepository {

    private final SysUserMapper sysUserMapper;
    private final SysUserInfoMapper sysUserInfoMapper;

    @Override
    public void save(SysUserEntity user) {
        sysUserMapper.insert(user);
    }

    @Override
    public void update(SysUserEntity user) {
        sysUserMapper.updateById(user);
    }

    @Override
    public void saveUserInfo(SysUserInfoEntity userInfo) {
        sysUserInfoMapper.insert(userInfo);
    }

    @Override
    public void updateUserInfo(SysUserInfoEntity userInfo) {
        sysUserInfoMapper.updateById(userInfo);
    }
}
