package org.company.finance.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.company.finance.common.util.R;
import org.company.finance.auth.interfaces.rest.request.SysLoginRequestVO;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:55
 *
 */
public interface SysUserService extends IService<SysUserEntity> {

    R<?> login(SysLoginRequestVO requestVO);
}
