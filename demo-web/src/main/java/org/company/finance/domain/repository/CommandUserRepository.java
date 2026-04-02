package org.company.finance.domain.repository;

import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.infrastructure.persistence.entity.SysUserInfoEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-29
 *
 */
public interface CommandUserRepository {
    void save(SysUserEntity user);
    
    void update(SysUserEntity user);
    
    void saveUserInfo(SysUserInfoEntity userInfo);
    
    void updateUserInfo(SysUserInfoEntity userInfo);
}
