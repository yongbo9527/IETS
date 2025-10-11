package org.company.finance.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:58
 *
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

}
