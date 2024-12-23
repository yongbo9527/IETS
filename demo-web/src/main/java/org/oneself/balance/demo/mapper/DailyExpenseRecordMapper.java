package org.oneself.balance.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-11-19 16:52
 *
 */
@Mapper
public interface DailyExpenseRecordMapper extends BaseMapper<DailyExpenseRecordEntity> {
    /**
     * 插入数据，存在则更新
     * @param entity
     */
    void insertOrUpdate(DailyExpenseRecordEntity entity);
}
