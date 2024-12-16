package org.oneself.balance.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-11-19 15:41
 *
 */
@Data
@TableName("daily_expense_record")
public class DailyExpenseRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Integer id;

    /**
     * 类目ID，关联base_tally_category表
     */
    private Integer categoryId;

    /**
     * 消费金额
     */
    private BigDecimal expenseAmount;

    /**
     * 消费日期
     */
    private String expenseDate;

    /**
     * 消费类型，1-支出，2-收入
     */
    private Integer expenseType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private String createName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除标记，0-未删除，1-已删除
     */
    private Integer delFlag;


}
