package org.oneself.balance.demo.service;

import com.baomidou.mybatisplus.extension.api.R;
import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;
import org.oneself.balance.demo.vo.balance.QueryBalanceVO;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:36
 */
public interface BalanceService {
    /**
     * 新增收支记录
     * @param entity
     */
    void addBalance(DailyExpenseRecordEntity entity);

    /**
     * 查询收支记录
     * @param vo
     * @return
     */
    R queryBalance(QueryBalanceVO vo);

    /**
     * 查询分类列表
     * @return
     */
    R queryTreeCategoryList();

    /**
     * 根据数据查询展示表格头信息
     * @param vo
     * @return
     */
    R queryBalanceShortHead(QueryBalanceVO vo);
}
