/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : redpagket

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2021-03-01 18:10:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rp_chat_group
-- ----------------------------
DROP TABLE IF EXISTS `rp_chat_group`;
CREATE TABLE `rp_chat_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_name` varchar(255) DEFAULT NULL COMMENT '群名称',
  `group_code` varchar(255) DEFAULT NULL COMMENT '群编码',
  `person_count` int(11) DEFAULT NULL COMMENT '群人数',
  `notice` varchar(255) DEFAULT NULL COMMENT '群公告',
  `reamrk` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `GROUP_CODE_UNIQUE` (`group_code`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='聊天群';

-- ----------------------------
-- Records of rp_chat_group
-- ----------------------------

-- ----------------------------
-- Table structure for rp_red_pagket
-- ----------------------------
DROP TABLE IF EXISTS `rp_red_pagket`;
CREATE TABLE `rp_red_pagket` (
  `id` int(11) NOT NULL,
  `red_pagket_code` varchar(255) NOT NULL COMMENT '红包编码',
  `group_code` varchar(255) NOT NULL COMMENT '聊天群编码',
  `user_code` varchar(255) NOT NULL COMMENT '用户编码',
  `money` double(11,2) DEFAULT NULL COMMENT '红包金额',
  `balance` double(11,2) DEFAULT NULL COMMENT '红包剩余未抢余额',
  `num` int(11) DEFAULT NULL COMMENT '发出的红包数',
  `grabed_num` int(11) DEFAULT NULL COMMENT '已抢红包数',
  `status` varchar(255) DEFAULT NULL COMMENT '红包状态，grabing：争抢中，finish：已抢完，timeout：已过期',
  `message` varchar(255) DEFAULT NULL COMMENT '提示语',
  `cover_url` varchar(255) DEFAULT NULL COMMENT '封面图片地址',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `end_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='红包信息表';

-- ----------------------------
-- Records of rp_red_pagket
-- ----------------------------

-- ----------------------------
-- Table structure for rp_red_pagket_child
-- ----------------------------
DROP TABLE IF EXISTS `rp_red_pagket_child`;
CREATE TABLE `rp_red_pagket_child` (
  `id` int(11) NOT NULL,
  `red_pagket_code` varchar(255) DEFAULT NULL COMMENT '红包编码',
  `user_code` varchar(255) DEFAULT NULL COMMENT '用户编码',
  `money` double(11,2) DEFAULT NULL COMMENT '金额',
  `status` int(1) DEFAULT NULL COMMENT '状态，已领取：1，未领取：0',
  `got_time` datetime DEFAULT NULL COMMENT '领取时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='红包子表，记录每个红包被领取的详情';

-- ----------------------------
-- Records of rp_red_pagket_child
-- ----------------------------

-- ----------------------------
-- Table structure for rp_user
-- ----------------------------
DROP TABLE IF EXISTS `rp_user`;
CREATE TABLE `rp_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) NOT NULL COMMENT '昵称',
  `user_code` varchar(255) NOT NULL COMMENT '用户编码，唯一',
  `balance` double(12,2) DEFAULT NULL COMMENT '余额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `USER_CODE_UNIQUE` (`user_code`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of rp_user
-- ----------------------------

-- ----------------------------
-- Table structure for rp_user_chat_group
-- ----------------------------
DROP TABLE IF EXISTS `rp_user_chat_group`;
CREATE TABLE `rp_user_chat_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(255) DEFAULT NULL COMMENT '用户编码',
  `group_code` varchar(255) DEFAULT NULL COMMENT '聊天群编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-聊天群对应关系表';

-- ----------------------------
-- Records of rp_user_chat_group
-- ----------------------------
