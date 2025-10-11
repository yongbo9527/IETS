package org.company.finance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:19
 *
 */
@Data
@TableName("base_tally_category")
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类目id，主键自增
     */
    @TableId
    private Integer id;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 类目图标
     */
    private String categoryIcon;

    /**
     * 父级类目id
     */
    private Integer parentId;

    /**
     * 消费类型，1-支出，2-收入
     */
    private Integer expenseType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private String updateName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 删除标识
     */
    private Integer delFlag;

    @TableField(exist = false)
    private List<CategoryEntity> list = new ArrayList<>();

}
