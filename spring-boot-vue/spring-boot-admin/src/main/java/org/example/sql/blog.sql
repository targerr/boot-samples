SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
                         `id` bigint(0) NOT NULL,
                         `user_id` bigint(0) NULL DEFAULT NULL,
                         `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                         `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                         `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                         `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (1, 1001, 'zhifou', '知否君', 'java开发与进阶', '2025-05-21 21:27:46', '2025-08-17 19:09:34', 'java');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
                            `id` bigint(0) NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名',
                            `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
                            `stock` int(0) NULL DEFAULT NULL COMMENT '商品库存',
                            `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品描述',
                            `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                            `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (5, '苹果', 1121.00, 2121, '苹果', '2025-09-10 22:41:13', '2025-09-10 22:41:13');
INSERT INTO `product` VALUES (6, '橘子', 121.00, 21, '橘子', '2025-09-10 22:47:59', '2025-09-10 22:47:59');
INSERT INTO `product` VALUES (7, '草莓', 3.00, 11121, '草莓', '2025-09-11 08:41:26', '2025-09-11 08:41:26');
INSERT INTO `product` VALUES (8, '桑葚', 2.00, 1212121, '桑葚', '2025-09-11 08:42:15', '2025-09-11 08:42:14');
INSERT INTO `product` VALUES (9, '西柚', 4.30, 6456, '西柚', '2025-09-11 08:42:59', '2025-09-11 08:42:58');
INSERT INTO `product` VALUES (10, '蜜柑', 4.00, 1000, '蜜柑', '2025-09-11 08:43:11', '2025-09-11 08:43:10');
INSERT INTO `product` VALUES (11, '葡萄柚', 121.00, 155, '', '2025-09-11 08:43:21', '2025-09-11 08:43:21');
INSERT INTO `product` VALUES (12, '脐橙', 2.63, 45451, '脐橙', '2025-09-11 08:44:48', '2025-09-11 08:44:48');
INSERT INTO `product` VALUES (13, '人参果', 9.90, 5623, '人参果', '2025-09-11 08:45:10', '2025-09-11 08:45:09');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint(0) NOT NULL,
                         `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `age` int(0) NULL DEFAULT NULL,
                         `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'zhifou', '知否', '123456', 18, '1001');
INSERT INTO `user` VALUES (2, 'libai', '李白', '123456', 19, '1002');
INSERT INTO `user` VALUES (3, 'dufu', '杜甫', '123456', 20, '1003');

SET FOREIGN_KEY_CHECKS = 1;