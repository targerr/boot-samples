SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) DEFAULT NULL,
  `order_status` int(1) DEFAULT NULL,
  `userid` varchar(64) DEFAULT NULL,
  `recv_name` varchar(255) DEFAULT NULL,
  `recv_address` varchar(255) DEFAULT NULL,
  `recv_mobile` varchar(255) DEFAULT NULL,
  `postage` float(255,0) DEFAULT NULL,
  `amout` float(255,0) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_promotion_seckill
-- ----------------------------
DROP TABLE IF EXISTS `t_promotion_seckill`;
CREATE TABLE `t_promotion_seckill` (
  `ps_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` int(11) NOT NULL,
  `ps_count` int(255) NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` int(255) DEFAULT NULL COMMENT '0-未开始 1-进行中  2-已结束',
  `current_price` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`ps_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;
