CREATE TABLE `base_token`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                   `token` varchar(1024) NOT NULL COMMENT '带有前缀的token字符串（Bearer 或者 Basic）',
                                   `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被删除',
                                   `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                   `create_user_id` bigint(20) NOT NULL DEFAULT 1 COMMENT '创建人id',
                                   `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `idx_base_token__user_id`(`user_id`) COMMENT '用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '用户登录系统后生成的token记录表，当用户修改密码后，之前生成的token都不能再使用了。' ROW_FORMAT = Dynamic;

