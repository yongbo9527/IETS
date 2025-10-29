package org.company.finance.domain.repository;

import org.company.finance.infrastructure.persistence.entity.SysUserEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-17 14:34
 *  @Description:
 *
 */
public interface QueryUserRepository {
    SysUserEntity findByUsername(String username);
}
