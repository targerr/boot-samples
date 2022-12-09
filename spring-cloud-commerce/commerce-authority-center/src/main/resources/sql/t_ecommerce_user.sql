-- 创建 t_ecommerce_user 数据表
CREATE TABLE IF NOT EXISTS `imooc_e_commerce`.`t_ecommerce_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(256) NOT NULL DEFAULT '' COMMENT 'MD5 加密之后的密码',
  `extra_info` varchar(1024) NOT NULL DEFAULT '' COMMENT '额外的信息',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='用户表';
