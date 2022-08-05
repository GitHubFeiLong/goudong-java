CREATE TABLE `base_api_resource` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `pattern` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路径Pattern',
  `method` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'GET' COMMENT '请求方式',
  `application_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'goudong-xxxx-server' COMMENT '接口所在应用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
  `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_base_api_resource_pattern_method_application_name` (`pattern`,`method`,`application_name`) USING BTREE COMMENT '唯一索引',
  KEY `idx_base_api_resource_application_name` (`application_name`) USING BTREE COMMENT '普通索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保存系统中所有api接口资源';