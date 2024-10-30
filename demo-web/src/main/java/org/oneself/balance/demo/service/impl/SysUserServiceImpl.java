package org.oneself.balance.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.oneself.balance.demo.entity.SysUserEntity;
import org.oneself.balance.demo.mapper.SysUserMapper;
import org.oneself.balance.demo.service.SysUserService;
import org.oneself.balance.demo.service.SysUserTokenService;
import org.oneself.balance.demo.vo.SysLoginRequestVO;
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
    public R login(SysLoginRequestVO requestVO) {
        LambdaQueryWrapper<SysUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserEntity::getUsername, requestVO.getUsername());
        SysUserEntity user = this.getOne(queryWrapper);

        //账号不存在、密码错误
        if(user == null || !user.getPassword().equals(new Sha256Hash(requestVO.getPassword(), user.getSalt()).toHex())) {
            return R.failed("账号或密码不正确");
        }

        //账号锁定
        if(user.getStatusFlag() == 0){
            return R.failed("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        R r = sysUserTokenService.createToken(user.getId());
        return r;
    }
}
