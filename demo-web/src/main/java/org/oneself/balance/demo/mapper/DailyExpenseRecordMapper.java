package org.oneself.balance.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;
import org.oneself.balance.demo.vo.balance.QueryBalanceVO;

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

    /**
     * 查询支出原始数据
     *
     * @param vo
     * @param queryBalanceVO
     * @return
     */
    Page<DailyExpenseRecordEntity> selectExpenseMetaData(@Param("page") Page<QueryBalanceVO> vo, @Param("vo")  QueryBalanceVO queryBalanceVO);
}
