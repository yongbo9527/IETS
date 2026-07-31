package org.company.finance.auth.domain.repository;

import org.company.finance.auth.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserInfoEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-17 14:34
 *  @Description:
 *
 */
public interface QueryUserRepository {
    SysUserEntity findByUsername(String username);
    
    SysUserEntity findById(Long id);
    
    SysUserInfoEntity findUserInfoByUserId(Long userId);
}
