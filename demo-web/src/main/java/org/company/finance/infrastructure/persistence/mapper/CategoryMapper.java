package org.company.finance.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:04
 *
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
    /**
     * 查询已有数据的分类
     * @param vo
     * @return
     */
    List<DataCategoryVO> selectDataCategory(QueryBalanceVO vo);
}
