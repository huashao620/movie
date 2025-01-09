/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : movie

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 19/12/2024 22:00:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电影名称',
  `introduction` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `img_url` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '海报',
  `director` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导演',
  `actor` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '演员',
  `up_date` date NULL DEFAULT NULL COMMENT '上映日期',
  `score` decimal(10, 1) NULL DEFAULT NULL COMMENT '评分',
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of movie
-- ----------------------------
INSERT INTO `movie` VALUES (1, '肖申克的救赎', '肖申克的救赎是一部由弗兰克·德拉邦特导演，蒂姆·罗宾斯和摩根·弗里曼主演的1994年美国犯罪剧情片。该片在豆瓣电影上获得了高达9.7的评分，有2824978人参与评价。影片讲述了银行家安迪在监狱中的经历，以及他与监狱大哥瑞德、典狱长和小偷兄弟之间的故事。这部作品深刻地探讨了希望、自由和人性的主题，强调了希望的力量可以让人获得真正的自由。\n\n', '1734426039613.jpg', '弗兰克·德拉邦特', '蒂姆·罗宾斯，摩根·弗里曼', '1994-11-16', 9.7, '励志');
INSERT INTO `movie` VALUES (2, '千寻千寻', '《千与千寻》是宫崎骏导演的杰作，荣获9.4的高分，并由柊瑠美、入野自由和夏木真理主演。该动画电影于2001年上映，是一部融合了剧情、动画和奇幻元素的日本作品。影片讲述了主人公千寻与小白、汤婆婆之间的冒险故事，深受观众喜爱，被誉为最好的宫崎骏和久石让的作品。', '1734488348017.jpg', '宫崎骏', '铃木千寻，日下千寻，悠木碧', '2001-11-13', 9.1, '友情');
INSERT INTO `movie` VALUES (3, '热辣滚烫', '《热辣滚烫》是由贾玲执导，孙集斌编剧，贾玲、雷佳音领衔主演，张小斐、赵海燕、张琪、许君聪、卜钰、朱天福、刘宏禄主演，杨紫、沙溢、魏翔、李雪琴、沈春阳、沈涛、马丽、乔杉等出演的喜剧电影，于2024年2月10日在中国大陆上映。。', '1734531259656.jpg', '贾玲', '雷佳音，张小斐、赵海燕、张琪、许君聪', '2024-02-10', 8.7, '励志');

-- ----------------------------
-- Table structure for movie_collect
-- ----------------------------
DROP TABLE IF EXISTS `movie_collect`;
CREATE TABLE `movie_collect`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '收藏用户',
  `movie_id` int NULL DEFAULT NULL COMMENT '收藏电影',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key3`(`movie_id`) USING BTREE,
  INDEX `key4`(`user_id`) USING BTREE,
  CONSTRAINT `key3` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `key4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of movie_collect
-- ----------------------------
INSERT INTO `movie_collect` VALUES (5, 4, 2);

-- ----------------------------
-- Table structure for movie_remark
-- ----------------------------
DROP TABLE IF EXISTS `movie_remark`;
CREATE TABLE `movie_remark`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论内容',
  `user_id` int NULL DEFAULT NULL COMMENT '评论用户',
  `to_user_id` int NULL DEFAULT NULL COMMENT '回复用户',
  `parent_id` int NULL DEFAULT NULL COMMENT '回复的消息',
  `movie_id` int NULL DEFAULT NULL COMMENT '评论电影',
  `create_time` datetime NULL DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key1`(`user_id`) USING BTREE,
  INDEX `key2`(`movie_id`) USING BTREE,
  CONSTRAINT `key1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `key2` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of movie_remark
-- ----------------------------
INSERT INTO `movie_remark` VALUES (5, '挺好', 6, NULL, 0, 1, '2024-12-17 20:13:25');
INSERT INTO `movie_remark` VALUES (11, '是的，同感', 4, 6, 5, 1, '2024-12-18 22:17:50');
INSERT INTO `movie_remark` VALUES (12, '大家好', 4, NULL, 0, 1, '2024-12-18 22:30:27');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别，可选值：男、女',
  `birth_date` date NULL DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `role` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色，可选值：管理员、用户',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录账号',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录密码',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态，可选值：正常、禁用',
  `freeze_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '禁用原因',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '管理员', '男', '1992-07-22', '15566667777', '管理员', 'admin', '123456', '正常', NULL, '2024-12-17 16:31:14');
INSERT INTO `user` VALUES (4, '张三', '男', '2024-12-10', '15555555555', '用户', 'zhangsan', '123456', '正常', '', '2024-12-17 16:43:36');
INSERT INTO `user` VALUES (6, '李四', '女', '2001-07-02', '16677778888', '用户', 'lisi', '123456', '正常', NULL, '2024-12-17 16:52:45');
INSERT INTO `user` VALUES (8, '王二', '男', '2024-12-10', '16677779999', '用户', 'wanger', '123456', '正常', '', '2024-12-18 22:12:16');

SET FOREIGN_KEY_CHECKS = 1;
