/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : localhost:3306
 Source Schema         : datascope_demo

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 09/04/2026 14:01:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_order`;
CREATE TABLE `biz_order` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                             `order_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
                             `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
                             `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
                             `dept_id` bigint(20) NOT NULL COMMENT '所属部门ID',
                             `created_by` bigint(20) NOT NULL COMMENT '创建者ID',
                             `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `del_flag` int(11) DEFAULT '0' COMMENT '删除标记(0-正常,1-删除)',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ----------------------------
-- Records of biz_order
-- ----------------------------
BEGIN;
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (1, 'ORD001', '客户A', 50000.00, 5, 4, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (2, 'ORD002', '客户B', 30000.00, 5, 4, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (3, 'ORD003', '客户C', 80000.00, 6, 5, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (4, 'ORD004', '客户D', 120000.00, 7, 6, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (5, 'ORD005', '客户E', 95000.00, 7, 6, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (6, 'ORD006', '客户F', 150000.00, 8, 7, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (7, 'ORD007', '客户G', 78000.00, 8, 7, '2026-04-09 09:18:50', 0);
INSERT INTO `biz_order` (`id`, `order_no`, `customer_name`, `amount`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (8, 'ORD008', '客户H', 60000.00, 4, 2, '2026-04-09 09:18:50', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
                            `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门名称',
                            `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门ID',
                            `sort` int(11) DEFAULT '0' COMMENT '排序',
                            `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (1, '总公司', 0, 1, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (2, '研发部', 1, 2, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (3, '销售部', 1, 3, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (4, '财务部', 1, 4, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (5, '研发一组', 2, 5, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (6, '研发二组', 2, 6, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (7, '销售一组', 3, 7, '2026-04-09 09:18:50');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `sort`, `created_date`) VALUES (8, '销售二组', 3, 8, '2026-04-09 09:18:50');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                            `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
                            `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
                            `data_scope_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'THIS_LEVEL' COMMENT '数据权限类型',
                            `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `name`, `code`, `data_scope_type`, `created_date`) VALUES (1, '管理员', 'ROLE_ADMIN', 'ALL', '2026-04-09 09:18:50');
INSERT INTO `sys_role` (`id`, `name`, `code`, `data_scope_type`, `created_date`) VALUES (2, '部门经理', 'ROLE_MANAGER', 'THIS_LEVEL_CHILDREN', '2026-04-09 09:18:50');
INSERT INTO `sys_role` (`id`, `name`, `code`, `data_scope_type`, `created_date`) VALUES (3, '普通用户', 'ROLE_USER', 'SELF', '2026-04-09 09:18:50');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                            `password` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                            `real_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '真实姓名',
                            `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
                            `created_by` bigint(20) DEFAULT NULL COMMENT '创建者ID',
                            `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `del_flag` int(11) DEFAULT '0' COMMENT '删除标记(0-正常,1-删除)',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (1, 'admin', '$2a$10$U5KE/RvBji6KnxIRkD10ouxoTIYba0f07Id/uduuzT2JL.zJ6mv5a', '系统管理员', 1, NULL, '2026-04-09 09:18:50', 0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (2, 'manager', '$2a$10$.njlPPGPXpt3W0As4PieuenQDuq8jX/a5GtMJuw6Hed6.isEMVpCG', '研发部经理', 2, 1, '2026-04-09 09:18:50', 0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (3, 'sales_mgr', '$2a$10$.njlPPGPXpt3W0As4PieuenQDuq8jX/a5GtMJuw6Hed6.isEMVpCG', '销售部经理', 3, 1, '2026-04-09 09:18:50', 0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (4, 'user1', '$2a$10$scsb.YrpFKFsLeSgL09T/O53M0OoIzMy3q/mdmnQF7t2UKMh7psmS', '研发工程师A', 5, 4, '2026-04-09 09:18:50', 0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (5, 'user2', '$2a$10$scsb.YrpFKFsLeSgL09T/O53M0OoIzMy3q/mdmnQF7t2UKMh7psmS', '研发工程师B', 6, 1, '2026-04-09 09:18:50', 0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (6, 'user3', '$2a$10$scsb.YrpFKFsLeSgL09T/O53M0OoIzMy3q/mdmnQF7t2UKMh7psmS', '销售代表A', 7, 1, '2026-04-09 09:18:50', 0);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `dept_id`, `created_by`, `created_date`, `del_flag`) VALUES (7, 'user4', '$2a$10$scsb.YrpFKFsLeSgL09T/O53M0OoIzMy3q/mdmnQF7t2UKMh7psmS', '销售代表B', 8, 1, '2026-04-09 09:18:50', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
