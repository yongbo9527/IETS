package org.company.finance.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.company.finance.infrastructure.persistence.entity.SysUserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserInfoMapper extends BaseMapper<SysUserInfoEntity> {
}
