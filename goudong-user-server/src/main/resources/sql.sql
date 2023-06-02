CREATE TABLE `base_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(32) NOT NULL COMMENT 'appId',
  `app_secret` varchar(32) NOT NULL COMMENT 'appSecret',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
  `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_id` (`app_id`) USING BTREE COMMENT '应用id唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='三方应用信息';

CREATE TABLE `base_app_open_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `open_id` varchar(32) NOT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
  `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_id_open_id` (`app_id`,`open_id`) USING BTREE COMMENT '同一个应用下openId唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户openId信息';
