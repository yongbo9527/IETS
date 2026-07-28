package org.company.finance.auth.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserTokenEntity;
import org.company.finance.common.util.R;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:36
 *
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

    /**
     * 生成token
     * @param userId  用户ID
     */
    R<?> createToken(long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(Long userId);
}
