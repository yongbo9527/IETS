package org.oneself.balance.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:19
 *
 */
@Data
@TableName("base_tally_category")
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    private String categoryName;
    private String categoryIcon;
    private String remark;
    private String createName;
    private String createTime;
    private String updateName;
    private String updateTime;
    private Integer delFlag;

}
