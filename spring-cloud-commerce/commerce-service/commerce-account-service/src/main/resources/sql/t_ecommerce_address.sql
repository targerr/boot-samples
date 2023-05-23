-- 创建 t_ecommerce_address 数据表
CREATE TABLE IF NOT EXISTS `imooc_e_commerce`.`t_ecommerce_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户 id',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `phone` varchar(64) NOT NULL DEFAULT '' COMMENT '电话号码',
  `province` varchar(64) NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(64) NOT NULL DEFAULT '' COMMENT '市',
  `address_detail` varchar(256) NOT NULL DEFAULT '' COMMENT '详细地址',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='用户地址表';
