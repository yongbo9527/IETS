package org.company.finance.auth.application.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.company.finance.auth.application.service.SysUserTokenService;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserTokenEntity;
import org.company.finance.auth.infrastructure.persistence.mapper.SysUserTokenMapper;
import org.company.finance.common.util.R;
import org.company.finance.common.util.TokenGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:39
 *
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserTokenEntity> implements SysUserTokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public R<?> createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = now.plusSeconds(EXPIRE);

        //判断是否生成过token
        SysUserTokenEntity tokenEntity = this.getById(userId);
        if(tokenEntity == null){
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setAccessToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setAccessExpireTime(expireTime);
            tokenEntity.setStatusFlag(1);
            tokenEntity.setTokenVersion(1);

            //保存token
            this.save(tokenEntity);
        }else{
            tokenEntity.setAccessToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setAccessExpireTime(expireTime);
            tokenEntity.setStatusFlag(1);

            //更新token
            this.updateById(tokenEntity);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", EXPIRE);
        return R.ok(map);
    }

    @Override
    public void logout(Long userId) {
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setAccessToken(null);
        tokenEntity.setAccessExpireTime(LocalDateTime.now());
        tokenEntity.setRefreshToken(null);
        tokenEntity.setRefreshExpireTime(LocalDateTime.now());
        tokenEntity.setStatusFlag(0);
        tokenEntity.setUpdateTime(LocalDateTime.now());
        this.updateById(tokenEntity);
    }
}
