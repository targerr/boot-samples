CREATE TABLE `sequence_role` (
                                 `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `sequence_role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '流水号规则id',
                                 `sequence_key` varchar(20) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '规则key',
                                 `sequence_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '名称',
                                 `sequence_prefix` varchar(10) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '前缀',
                                 `time_format` varchar(20) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '时间格式',
                                 `sequence_length` tinyint(3) unsigned NOT NULL DEFAULT '4' COMMENT '流水号长度',
                                 `sequence_begin` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '流水号开始',
                                 `sequence_interval` tinyint(4) NOT NULL DEFAULT '1' COMMENT '流水号步长',
                                 `created_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
                                 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updated_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
                                 `updated_at` timestamp NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '更新时间',
                                 `del_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '删除(0:正常,1已删除)',
                                 `sys_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '系统最后修改时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='流水号规则表';

CREATE TABLE `sequence_record` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `sequence_record_id` bigint(20) NOT NULL COMMENT '流水号id',
                                   `sequence_record_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '由流水号规则key、前缀、日期组成的名称',
                                   `current_value` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '当前值',
                                   `sequence_interval` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '步长',
                                   `created_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
                                   `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `updated_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
                                   `updated_at` timestamp NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '更新时间',
                                   `del_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '删除(0:正常,1已删除)',
                                   `sys_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '系统最后修改时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1839133083601780739 DEFAULT CHARSET=utf8 COMMENT='流水号表';



INSERT INTO `jfinal_demo`.`sequence_role` (`id`, `sequence_role_id`, `sequence_key`, `sequence_name`, `sequence_prefix`, `time_format`, `sequence_length`, `sequence_begin`, `sequence_interval`, `created_by`, `created_at`, `updated_by`, `updated_at`, `del_flag`, `sys_at`) VALUES (1, 1, 'SPDJ', '审批单据', 'SPDJ', 'yyyyMMdd', 4, 1, 1, 0, '2023-08-07 10:01:21', 0, '1970-01-01 08:00:01', 0, '2024-09-26 10:28:37');
INSERT INTO `jfinal_demo`.`sequence_role` (`id`, `sequence_role_id`, `sequence_key`, `sequence_name`, `sequence_prefix`, `time_format`, `sequence_length`, `sequence_begin`, `sequence_interval`, `created_by`, `created_at`, `updated_by`, `updated_at`, `del_flag`, `sys_at`) VALUES (2, 2, 'HTDJ', '合同单据', 'HTDJ', 'yyyyMMdd', 4, 1, 1, 0, '2023-08-07 10:01:21', 0, '1970-01-01 08:00:01', 0, '2024-09-26 10:28:37');
INSERT INTO `jfinal_demo`.`sequence_role` (`id`, `sequence_role_id`, `sequence_key`, `sequence_name`, `sequence_prefix`, `time_format`, `sequence_length`, `sequence_begin`, `sequence_interval`, `created_by`, `created_at`, `updated_by`, `updated_at`, `del_flag`, `sys_at`) VALUES (3, 3, 'YZRW', '用章任务', 'YZRW', 'yyyyMMdd', 4, 1, 1, 0, '2023-08-07 10:01:21', 0, '1970-01-01 08:00:01', 0, '2024-09-26 10:28:37');

INSERT INTO `jfinal_demo`.`sequence_record` (`id`, `sequence_record_id`, `sequence_record_name`, `current_value`, `sequence_interval`, `created_by`, `created_at`, `updated_by`, `updated_at`, `del_flag`, `sys_at`) VALUES (1839133083601780737, 1839133083592527872, 'SPDJ20240926', 650060, 1, 0, '2024-09-26 10:41:11', 0, '1970-01-01 08:00:01', 0, '2024-09-26 15:07:25');
INSERT INTO `jfinal_demo`.`sequence_record` (`id`, `sequence_record_id`, `sequence_record_name`, `current_value`, `sequence_interval`, `created_by`, `created_at`, `updated_by`, `updated_at`, `del_flag`, `sys_at`) VALUES (1839133083601780738, 1839200133497180160, 'HTDJ20240926', 650020, 1, 0, '2024-09-26 15:07:37', 0, '1970-01-01 08:00:01', 0, '2024-09-26 15:07:37');