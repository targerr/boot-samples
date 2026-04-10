/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : localhost:3306
 Source Schema         : permission

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 07/04/2026 11:07:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_acl
-- ----------------------------
DROP TABLE IF EXISTS `sys_acl`;
CREATE TABLE `sys_acl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '权限码',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '权限名称',
  `acl_module_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限所在的权限模块id',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT '请求的url, 可以填正则表达式',
  `type` int(11) NOT NULL DEFAULT '3' COMMENT '类型，1：菜单，2：按钮，3：其他',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：正常，0：冻结',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限在当前模块下的顺序，由小到大',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一个更新者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_acl
-- ----------------------------
BEGIN;
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (13, 'DASHBOARD_PAGE', '首页', 2, '/dashboard', 1, 1, 1, '首页页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (14, 'USER_PAGE', '用户管理', 2, '/system/user', 1, 1, 2, '用户管理页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (15, 'DEPT_PAGE', '部门管理', 3, '/system/dept', 1, 1, 3, '部门管理页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (16, 'ACL_PAGE', '权限管理', 4, '/system/acl', 1, 1, 4, '权限管理页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (17, 'ROLE_PAGE', '角色管理', 5, '/system/role', 1, 1, 5, '角色管理页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (18, 'LOG_PAGE', '操作日志', 6, '/system/log', 1, 1, 6, '操作日志页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (19, 'GENERATOR_PAGE', '代码生成器', 8, '/generator/index', 1, 1, 7, '代码生成器页面', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (20, 'USER_ADD', '新增用户', 2, '/api/system/user', 2, 1, 11, '新增用户按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (21, 'USER_EDIT', '编辑用户', 2, '/api/system/user', 2, 1, 12, '编辑用户按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (22, 'USER_DELETE', '删除用户', 2, '/api/system/user', 2, 1, 13, '删除用户按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (23, 'USER_STATUS', '用户状态', 2, '/api/system/user', 2, 1, 14, '修改用户状态权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (24, 'DEPT_ADD', '新增部门', 3, '/api/system/dept', 2, 1, 21, '新增部门按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (25, 'DEPT_EDIT', '编辑部门', 3, '/api/system/dept', 2, 1, 22, '编辑部门按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (26, 'DEPT_DELETE', '删除部门', 3, '/api/system/dept', 2, 1, 23, '删除部门按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (27, 'ACL_ADD', '新增权限', 4, '/api/system/acl', 2, 1, 31, '新增权限按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (28, 'ACL_EDIT', '编辑权限', 4, '/api/system/acl', 2, 1, 32, '编辑权限按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (29, 'ACL_DELETE', '删除权限', 4, '/api/system/acl', 2, 1, 33, '删除权限按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (30, 'ROLE_ADD', '新增角色', 5, '/api/system/role', 2, 1, 41, '新增角色按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (31, 'ROLE_EDIT', '编辑角色', 5, '/api/system/role', 2, 1, 42, '编辑角色按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (32, 'ROLE_DELETE', '删除角色', 5, '/api/system/role', 2, 1, 43, '删除角色按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (33, 'ROLE_ASSIGN_ACL', '分配权限', 5, '/api/system/roleAcl', 2, 1, 44, '角色分配权限按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (34, 'ROLE_ASSIGN_USER', '分配用户', 5, '/api/system/roleUser', 2, 1, 45, '角色分配用户按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (35, 'LOG_RESTORE', '日志复原', 6, '/api/system/log', 2, 1, 51, '日志复原按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl` (`id`, `code`, `name`, `acl_module_id`, `url`, `type`, `status`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (36, 'GENERATOR_CODE', '生成代码', 8, '/api/generator', 2, 1, 61, '生成代码按钮权限', 'system', '2026-04-03 16:25:16', '127.0.0.1');
COMMIT;

-- ----------------------------
-- Table structure for sys_acl_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_acl_module`;
CREATE TABLE `sys_acl_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限模块id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '权限模块名称',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级权限模块id',
  `level` varchar(200) NOT NULL DEFAULT '' COMMENT '权限模块层级',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限模块在当前层级下的顺序，由小到大',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：正常，0：冻结',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次操作时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_acl_module
-- ----------------------------
BEGIN;
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (1, '系统管理', 0, '0', 1, 1, '系统管理模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (2, '用户管理', 1, '0.1', 1, 1, '用户管理子模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (3, '部门管理', 1, '0.1', 2, 1, '部门管理子模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (4, '权限管理', 1, '0.1', 3, 1, '权限管理子模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (5, '角色管理', 1, '0.1', 4, 1, '角色管理子模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (6, '操作日志', 1, '0.1', 5, 1, '操作日志子模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (7, '开发工具', 0, '0', 2, 1, '开发工具模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (8, '代码生成器', 7, '0.7', 1, 1, '代码生成器子模块', 'system', '2026-04-03 16:25:16', '127.0.0.1');
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (12, '测试管理', 0, '0', 0, 1, '测试', '', '2023-10-24 04:45:08', '');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '部门名称',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级部门id',
  `level` varchar(200) NOT NULL DEFAULT '' COMMENT '部门层级',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '部门在当前层级下的顺序，由小到大',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次操作时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (1, '技术部', 0, '0', 1, '技术部', 'system', '2017-10-11 07:21:40', '127.0.0.1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (2, '后端开发', 1, '0.1', 1, '后端', 'system-update', '2017-10-12 07:56:16', '127.0.0.1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (4, 'UI设计', 1, '0.1', 3, '', 'system', '2017-10-12 07:55:43', '127.0.0.1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (11, '产品部', 0, '0', 2, '', 'Admin', '2017-10-16 22:52:29', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (12, '客服部', 0, '0', 4, '', 'Admin', '2017-10-17 00:22:55', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (16, '销售部', 0, '0', 0, '13143', '', '2023-10-19 02:10:42', '');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (17, '销售部子部门', 16, '0.16', 1, '子部门', '', '2023-10-19 03:35:41', '');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (18, '销售部子子部门', 17, '0.16.17', 1, '子部门', '', '2023-10-19 03:15:30', '');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (19, '运维部', 1, '0.0', 4, '', '6', '2026-04-03 16:41:29', '127.0.0.1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (20, '前端部门', 1, '0.0', 1, '', '1', '2026-04-04 00:30:04', '127.0.0.1');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (21, '产品1部', 11, '0.0', 0, '', '1', '2026-04-04 00:48:31', '127.0.0.1');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系',
  `target_id` int(11) NOT NULL COMMENT '基于type后指定的对象id，比如用户、权限、角色等表的主键',
  `old_value` text COMMENT '旧值',
  `new_value` text COMMENT '新值',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '当前是否复原过，0：没有，1：复原过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (2, 1, 12, '', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":3,\"operator\":\"Admin\",\"operateTime\":1508166002610,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:00:03', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (3, 1, 12, '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":3,\"operator\":\"Admin\",\"operateTime\":1508166003000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508166009313,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:00:09', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (4, 2, 4, '', '{\"id\":4,\"username\":\"Kate\",\"telephone\":\"13144445555\",\"mail\":\"kate@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166166297,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:02:46', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (5, 2, 4, '{\"id\":4,\"username\":\"Kate\",\"telephone\":\"13144445555\",\"mail\":\"kate@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166166000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":4,\"username\":\"Kate\",\"telephone\":\"13144445555\",\"mail\":\"kate@qq.com\",\"deptId\":1,\"status\":1,\"remark\":\"sss\",\"operator\":\"Admin\",\"operateTime\":1508166171320,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:02:51', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (6, 3, 10, '', '{\"id\":10,\"name\":\"运维管理\",\"parentId\":0,\"level\":\"0\",\"seq\":5,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166212527,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:03:33', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (7, 3, 10, '{\"id\":10,\"name\":\"运维管理\",\"parentId\":0,\"level\":\"0\",\"seq\":5,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166213000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":10,\"name\":\"运维管理\",\"parentId\":0,\"level\":\"0\",\"seq\":6,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166217376,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:03:37', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (8, 3, 11, '', '{\"id\":11,\"name\":\"权限更新记录管理\",\"parentId\":6,\"level\":\"0.6\",\"seq\":4,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166246805,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:04:07', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (9, 4, 10, '', '{\"id\":10,\"code\":\"20171016230429_8\",\"name\":\"进入权限更新记录页面\",\"aclModuleId\":1,\"url\":\"/sys/log/log.page\",\"type\":1,\"status\":1,\"seq\":1,\"operator\":\"Admin\",\"operateTime\":1508166269419,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:04:29', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (10, 4, 10, '{\"id\":10,\"code\":\"20171016230429_8\",\"name\":\"进入权限更新记录页面\",\"aclModuleId\":1,\"url\":\"/sys/log/log.page\",\"type\":1,\"status\":1,\"seq\":1,\"operator\":\"Admin\",\"operateTime\":1508166269000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":10,\"name\":\"进入权限更新记录页面\",\"aclModuleId\":11,\"url\":\"/sys/log/log.page\",\"type\":1,\"status\":1,\"seq\":1,\"operator\":\"Admin\",\"operateTime\":1508166288589,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:04:49', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (11, 5, 5, '', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166301130,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:05:01', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (12, 5, 5, '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166301000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508166307317,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:05:07', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (13, 6, 4, '[7,8,9]', '[7,8,9,10]', 'Admin', '2017-10-16 23:34:39', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (14, 7, 4, '[1]', '[1,4]', 'Admin', '2017-10-16 23:34:44', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (15, 2, 5, '', '{\"id\":5,\"username\":\"服务员A\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170918338,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:21:58', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (16, 2, 5, '{\"id\":5,\"username\":\"服务员A\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170918000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"username\":\"服务员B\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170924698,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:05', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (17, 2, 5, '{\"id\":5,\"username\":\"服务员B\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170925000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"username\":\"服务员A\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170934791,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:15', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (18, 1, 12, '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508166009000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":12,\"name\":\"客服部A\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508170966051,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:46', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (19, 1, 12, '{\"id\":12,\"name\":\"客服部A\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508170966000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508170975242,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:55', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (20, 5, 5, '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508166307000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"name\":\"运维管理员A\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508170997531,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:23:18', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (21, 5, 5, '{\"id\":5,\"name\":\"运维管理员A\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508170998000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508171007651,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:23:28', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (22, 7, 4, '[1,4]', '[1,4,2,3,5]', 'Admin', '2017-10-17 00:23:53', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (23, 7, 4, '[1,4,2,3,5]', '[1,4]', 'Admin', '2017-10-17 00:24:04', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (24, 6, 5, '[]', '[7,8,9,10]', 'Admin', '2017-10-17 00:24:23', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (25, 6, 5, '[7,8,9,10]', '[]', 'Admin', '2017-10-17 00:24:34', '0:0:0:0:0:0:0:1', 1);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (26, 2, 6, '{\"deptId\":1,\"id\":6,\"mail\":\"123@qq.com\",\"operateIp\":\"\",\"operateTime\":\"2023-10-18 02:18:33\",\"operator\":\"\",\"password\":\"dbff702abd533721068083076d9b613d\",\"remark\":\"13143\",\"status\":0,\"telephone\":\"18806513871\",\"username\":\"tom\"}', '{\"deptId\":1,\"id\":6,\"mail\":\"123@qq.com\",\"operateIp\":\"\",\"operateTime\":\"2023-10-18 02:18:33\",\"operator\":\"\",\"password\":\"dbff702abd533721068083076d9b613d\",\"remark\":\"13143\",\"status\":1,\"telephone\":\"18806513871\",\"username\":\"tom\"}', '1', '2026-04-03 16:12:27', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (27, 7, 5, '[]', '[7,8,19,36]', '6', '2026-04-03 16:40:09', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (28, 7, 5, '[7,8,19,36]', '[7,8,19,36]', '6', '2026-04-03 16:47:45', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (29, 6, 5, '[]', '[1,2,3,4,5,6,7]', '6', '2026-04-03 16:47:52', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (30, 7, 6, '[13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36]', '[13,14,20,7,8,19,36]', '6', '2026-04-03 17:00:22', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (31, 6, 6, '[6]', '[1,2,3,4,5,6,7]', '6', '2026-04-03 17:00:32', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (32, 7, 6, '[13,14,20,7,8,19,36]', '[13,14,20,21,22,7,8,19,36]', '1', '2026-04-03 17:11:01', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (33, 7, 1, '[]', '[1,2,13,14,20,21,22,23,3,15,24,25,26,4,16,27,28,29,5,17,30,31,32,33,34,6,18,35,7,8,19,36,12]', '1', '2026-04-04 00:32:02', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (34, 6, 6, '[1,2,3,4,5,6,7]', '[5]', '1', '2026-04-04 00:56:40', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (35, 6, 6, '[5]', '[6]', '1', '2026-04-04 00:57:53', '127.0.0.1', 0);
INSERT INTO `sys_log` (`id`, `type`, `target_id`, `old_value`, `new_value`, `operator`, `operate_time`, `operate_ip`, `status`) VALUES (36, 7, 6, '[13,14,20,21,22,7,8,19,36]', '[13,14,20,21,7,8,19,36]', '1', '2026-04-04 00:57:57', '127.0.0.1', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(20) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '角色的类型，1：管理员角色，2：其他',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：可用，0：冻结',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (1, '产品管理员', 1, 1, '', 'Admin', '2017-10-15 12:42:47', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (2, '订单管理员', 1, 1, '', 'Admin', '2017-10-15 12:18:59', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (3, '公告管理员', 1, 1, '', 'Admin', '2017-10-15 12:19:10', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (4, '权限管理员', 1, 1, '', 'Admin', '2017-10-15 21:30:36', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (5, '运维管理员', 1, 1, '运维', 'Admin', '2017-10-17 00:23:28', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (6, '测试角色', 1, 1, '', '', '2023-10-20 01:32:17', '');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_acl
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_acl`;
CREATE TABLE `sys_role_acl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `acl_id` int(11) NOT NULL COMMENT '权限id',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
  `operate_ip` varchar(200) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_acl
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (23, 4, 13, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (24, 4, 14, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (25, 4, 15, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (26, 4, 16, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (27, 4, 17, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (28, 4, 18, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (29, 4, 19, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (30, 4, 20, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (31, 4, 21, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (32, 4, 22, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (33, 4, 23, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (34, 4, 24, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (35, 4, 25, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (36, 4, 26, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (37, 4, 27, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (38, 4, 28, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (39, 4, 29, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (40, 4, 30, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (41, 4, 31, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (42, 4, 32, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (43, 4, 33, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (44, 4, 34, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (45, 4, 35, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (46, 4, 36, 'system', '2026-04-03 16:25:59', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (75, 5, 7, '6', '2026-04-03 16:47:45', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (76, 5, 8, '6', '2026-04-03 16:47:45', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (77, 5, 19, '6', '2026-04-03 16:47:45', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (78, 5, 36, '6', '2026-04-03 16:47:45', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (95, 1, 1, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (96, 1, 2, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (97, 1, 13, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (98, 1, 14, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (99, 1, 20, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (100, 1, 21, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (101, 1, 22, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (102, 1, 23, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (103, 1, 3, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (104, 1, 15, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (105, 1, 24, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (106, 1, 25, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (107, 1, 26, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (108, 1, 4, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (109, 1, 16, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (110, 1, 27, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (111, 1, 28, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (112, 1, 29, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (113, 1, 5, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (114, 1, 17, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (115, 1, 30, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (116, 1, 31, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (117, 1, 32, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (118, 1, 33, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (119, 1, 34, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (120, 1, 6, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (121, 1, 18, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (122, 1, 35, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (123, 1, 7, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (124, 1, 8, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (125, 1, 19, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (126, 1, 36, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (127, 1, 12, '1', '2026-04-04 00:32:02', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (128, 6, 13, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (129, 6, 14, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (130, 6, 20, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (131, 6, 21, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (132, 6, 7, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (133, 6, 8, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (134, 6, 19, '1', '2026-04-04 00:57:57', '127.0.0.1');
INSERT INTO `sys_role_acl` (`id`, `role_id`, `acl_id`, `operator`, `operate_time`, `operate_ip`) VALUES (135, 6, 36, '1', '2026-04-04 00:57:57', '127.0.0.1');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (16, 4, 1, 'Admin', '2017-10-17 00:24:04', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (17, 4, 4, 'Admin', '2017-10-17 00:24:04', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (24, 5, 1, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (25, 5, 2, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (26, 5, 3, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (27, 5, 4, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (28, 5, 5, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (29, 5, 6, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (30, 5, 7, '6', '2026-04-03 16:47:52', '127.0.0.1');
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `operator`, `operate_time`, `operate_ip`) VALUES (39, 6, 6, '1', '2026-04-04 00:57:53', '127.0.0.1');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名称',
  `telephone` varchar(13) NOT NULL DEFAULT '' COMMENT '手机号',
  `mail` varchar(20) NOT NULL DEFAULT '' COMMENT '邮箱',
  `password` varchar(40) NOT NULL DEFAULT '' COMMENT '加密后的密码',
  `dept_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户所在部门的id',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：正常，0：冻结状态，2：删除',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (1, 'Admin', '18612344321', 'admin@qq.com', '25d55ad283aa400af464c76d713c07ad', 1, 1, 'admin', 'system', '2017-10-13 08:46:16', '127.0.0.1');
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (2, 'Jimin', '13188889999', '123@qq.com', '25d55ad283aa400af464c76d713c07ad', 1, 1, 'jimin.zheng', 'Admin', '2017-10-14 14:45:19', '127.0.0.1');
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (3, 'Jimmy', '13812344311', 'jimmy@qq.com', '25d55ad283aa400af464c76d713c07ad', 2, 1, '', 'Admin', '2017-10-16 12:57:35', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (4, 'Kate', '13144445555', 'kate@qq.com', '25d55ad283aa400af464c76d713c07ad', 2, 1, 'sss', 'Admin', '2017-10-16 23:02:51', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (5, '服务员A', '18677778888', 'service@qq.com', '25d55ad283aa400af464c76d713c07ad', 12, 1, '', 'Admin', '2017-10-17 00:22:15', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (6, 'tom', '18806513871', '123@qq.com', '25d55ad283aa400af464c76d713c07ad', 1, 1, '13143', '', '2023-10-18 02:18:33', '');
INSERT INTO `sys_user` (`id`, `username`, `telephone`, `mail`, `password`, `dept_id`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES (7, 'ruyi', '17179794057', '', 'e10adc3949ba59abbe56e057f20f883e', 19, 1, '', '6', '2026-04-03 16:41:06', '127.0.0.1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
