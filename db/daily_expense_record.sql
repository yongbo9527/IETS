CREATE TABLE `daily_expense_record` (
                                      `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `category_id` int NOT NULL COMMENT '类目ID，关联base_tally_category表',
                                      `expense_amount` decimal(10,2) NOT NULL COMMENT '消费金额',
                                      `expense_date` date NOT NULL COMMENT '消费日期',
                                      `expense_type` tinyint(1) DEFAULT '1' COMMENT '消费类型，1-支出，2-收入',
                                      `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                      `create_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者',
                                      `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `del_flag` tinyint DEFAULT '0' COMMENT '删除标记，0-未删除，1-已删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      KEY `idx_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='每日消费记录表';