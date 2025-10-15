/*
 Navicat MySQL Dump SQL

 Source Server         : localhost_windows
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3308
 Source Schema         : balance

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 13/10/2025 09:34:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_tally_category
-- ----------------------------
DROP TABLE IF EXISTS `base_tally_category`;
CREATE TABLE `base_tally_category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类目名称',
  `category_icon` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类目图标',
  `parent_id` int NULL DEFAULT NULL,
  `expense_type` tinyint NULL DEFAULT NULL COMMENT '消费类型，1-支出，2-收入',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账单类目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_tally_category
-- ----------------------------
INSERT INTO `base_tally_category` VALUES (1, '餐饮美食', '餐饮美食', 0, 1, '🚌', NULL, '2024-10-26 15:15:56', NULL, '2024-12-26 11:51:48', 0);
INSERT INTO `base_tally_category` VALUES (2, '服饰装扮', '服饰装扮', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (3, '日用百货', '日用百货', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (4, '家居家装', '家居家装', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (5, '数码电器', '数码电器', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (6, '运动户外', '运动户外', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (7, '美容美发', '美容美发', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (8, '母婴亲子', '母婴亲子', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (9, '宠物', '宠物', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (10, '交通出行', '交通出行', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (11, '爱车养车', '爱车养车', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (12, '住房物业', '住房物业', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (13, '酒店旅游', '酒店旅游', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (14, '文化休闲', '文化休闲', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (15, '教育培训', '教育培训', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (16, '医疗健康', '医疗健康', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (17, '生活服务', '生活服务', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (18, '公共服务', '公共服务', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (19, '商业服务', '商业服务', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (20, '公益捐赠', '公益捐赠', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (21, '投资理财', '投资理财', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (22, '保险', '保险', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (23, '信用借还', '信用借还', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (24, '充值缴费', '充值缴费', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:18', 0);
INSERT INTO `base_tally_category` VALUES (25, '收入', '收入', 0, 2, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:44:18', 0);
INSERT INTO `base_tally_category` VALUES (26, '支出', '支出', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:44:16', 0);
INSERT INTO `base_tally_category` VALUES (27, '转账红包', '转账红包', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (28, '其他', '其他', 0, 1, NULL, NULL, '2024-10-26 15:15:56', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (29, '早餐', '早餐', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (30, '午餐', '午餐', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (31, '晚餐', '晚餐', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (32, '咖啡牛奶', '咖啡牛奶', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (33, '炸鸡汉堡', '炸鸡汉堡', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:19', 0);
INSERT INTO `base_tally_category` VALUES (34, '烘焙糕点', '烘焙糕点', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (35, '生鲜水果', '生鲜水果', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (36, '零食', '零食', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (37, '餐饮其他', '餐饮其他', 1, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2025-09-11 17:07:51', 0);
INSERT INTO `base_tally_category` VALUES (38, '加油充电', '加油充电', 11, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (39, '停车', '停车', 11, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (40, '车辆养护', '车辆养护', 11, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (41, '车类其他', '车类其他', 11, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2025-09-11 17:09:22', 0);
INSERT INTO `base_tally_category` VALUES (42, '打车租车', '打车租车', 10, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:20', 0);
INSERT INTO `base_tally_category` VALUES (43, '公交地铁', '公交地铁', 10, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (44, '共享单车', '共享单车', 10, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (45, '飞机', '飞机', 10, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (46, '火车', '火车', 10, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (47, '交通其他', '交通其他', 10, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2025-09-11 17:09:41', 0);
INSERT INTO `base_tally_category` VALUES (48, '电费', '电费', 24, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (49, '燃气费', '燃气费', 24, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (50, '水费', '水费', 24, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (51, '手机充值', '手机充值', 24, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (52, '缴费其他', '缴费其他', 24, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2025-09-11 17:10:06', 0);
INSERT INTO `base_tally_category` VALUES (53, '账户存取', '账户存取', 0, 1, NULL, NULL, '2024-12-10 15:05:12', NULL, '2024-12-23 16:37:21', 0);
INSERT INTO `base_tally_category` VALUES (54, '工资', '工资', 25, 2, NULL, NULL, NULL, NULL, '2024-12-23 20:52:14', 0);
INSERT INTO `base_tally_category` VALUES (55, '红包', '红包', 25, 2, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `base_tally_category` VALUES (56, '礼金', '礼金', 25, 2, NULL, NULL, NULL, NULL, '2024-12-23 20:52:48', 0);
INSERT INTO `base_tally_category` VALUES (57, '收入其他', '收入其他', 25, 2, NULL, NULL, NULL, NULL, '2025-09-11 17:10:23', 0);

SET FOREIGN_KEY_CHECKS = 1;
