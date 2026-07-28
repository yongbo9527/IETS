package org.company.finance.auth.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.company.finance.auth.interfaces.rest.request.SysLoginRequestVO;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.common.util.R;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:55
 *
 */
public interface SysUserService extends IService<SysUserEntity> {

    R<?> login(SysLoginRequestVO requestVO);
}
