package org.company.finance.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.common.util.R;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.infrastructure.persistence.mapper.SysUserMapper;
import org.company.finance.application.service.SysUserService;
import org.company.finance.application.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:55
 *
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public R<?> login(SysLoginRequestVO requestVO) {
        LambdaQueryWrapper<SysUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserEntity::getUsername, requestVO.getUsername());
        SysUserEntity user = this.getOne(queryWrapper);

        //账号不存在
        if(user == null) {
            return R.failed("账号或密码不正确");
        }

        //账号锁定
        if(user.getStatusFlag() == 0){
            return R.failed("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        return sysUserTokenService.createToken(user.getId());
    }
}
