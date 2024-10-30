package org.oneself.balance.demo.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.oneself.balance.demo.entity.SysUserEntity;
import org.oneself.balance.demo.vo.SysLoginRequestVO;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:55
 *
 */
public interface SysUserService extends IService<SysUserEntity> {

    R login(SysLoginRequestVO requestVO);
}
