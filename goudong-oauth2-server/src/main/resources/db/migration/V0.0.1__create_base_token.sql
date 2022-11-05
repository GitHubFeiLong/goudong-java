DROP TABLE IF EXISTS `base_token`;
CREATE TABLE `base_token` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
      `access_token` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '短期token',
      `refresh_token` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '刷新token',
      `access_expires` datetime NOT NULL COMMENT 'access_token有效截止时长，APP上允许在线久一点。',
      `refresh_expires` datetime NOT NULL COMMENT 'refresh_token有效截止时长',
      `user_id` bigint(20) NOT NULL COMMENT '用户id',
      `client_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端类型',
      `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
      `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
      PRIMARY KEY (`id`),
      UNIQUE KEY `uk_base_token_access_token` (`access_token`) USING BTREE COMMENT '访问令牌唯一',
      UNIQUE KEY `uk_base_token_refresh_token` (`refresh_token`) COMMENT '刷新令牌唯一',
      UNIQUE KEY `uk_base_token_user_id_client_type_deleted` (`user_id`,`client_type`,`deleted`) USING BTREE COMMENT 'user_id,client_type,deleted组合唯一',
      KEY `idx_base_token_access_token` (`access_token`) USING BTREE COMMENT '访问令牌索引',
      KEY `idx_base_token_refresh_token` (`refresh_token`) USING BTREE COMMENT '刷新令牌索引'
) ENGINE=InnoDB AUTO_INCREMENT=1483961318754095105 DEFAULT CHARSET=utf8 COMMENT='用户token记录';