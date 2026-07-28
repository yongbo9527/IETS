package org.company.finance.tally.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.company.finance.tally.interfaces.rest.request.QueryBalanceVO;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-11-19 16:52
 *
 */
@Mapper
public interface ExpenseRecordMapper extends BaseMapper<ExpenseRecordDO> {
    /**
     * 查询支出原始数据
     *
     * @param vo
     * @param queryBalanceVO
     * @return
     */
    Page<ExpenseRecordDO> selectExpenseMetaData(@Param("page") Page<QueryBalanceVO> vo, @Param("vo") QueryBalanceVO queryBalanceVO);
}
