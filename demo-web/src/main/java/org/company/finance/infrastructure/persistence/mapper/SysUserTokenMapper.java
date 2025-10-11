package org.company.finance.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.company.finance.infrastructure.persistence.entity.SysUserTokenEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:41
 *
 */
@Mapper
public interface SysUserTokenMapper extends BaseMapper<SysUserTokenEntity> {
}
