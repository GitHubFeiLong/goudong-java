DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
     `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
     `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
     `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
     `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
     `valid_time` datetime NOT NULL DEFAULT '9999-12-31 23:59:59' COMMENT '有效截止时间',
     `qq_open_id` varchar(255) DEFAULT NULL COMMENT '账户关联qq的openId',
     `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
     `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
     PRIMARY KEY (`id`) USING BTREE,
     KEY `idx_base_user_username` (`username`),
     KEY `idx_base_user_email` (`email`),
     KEY `idx_base_user_phone` (`phone`),
     KEY `idx_base_user_qq_open_id` (`qq_open_id`),
     UNIQUE KEY `uq_base_user_username` (`username`,`deleted`) USING BTREE COMMENT '用户,用户名唯一键',
     UNIQUE KEY `uq_base_user_phone` (`phone`,`deleted`) USING BTREE COMMENT '用户，手机号唯一键',
     UNIQUE KEY `uq_base_user_email` (`email`,`deleted`) USING BTREE COMMENT '用户，邮箱唯一键',
     UNIQUE KEY `uq_base_user_qq_open_id` (`qq_open_id`,`deleted`) USING BTREE COMMENT '用户，qq_open_id唯一键'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户基本信息表';
INSERT INTO `base_user`
VALUES (1, 'admin', '$2a$10$DI2GDONVUKrDKcV4C2iAq.8OJ70J5qvOqbm1nA8EF6pQfikbnPdLu', '@admin', 'admin_phone', '超级管理员', '系统预置超级管理员',
        '9999-12-31 23:59:59', NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);
INSERT INTO `base_user`
VALUES (2, 'knife4j', '$2a$10$PVaGIUXolMkMVJIjTSZbGugSPR47LvfUkSzhqO0Ese.mqypFYuJtS', '@Knife4j', 'Knife4j_phone', 'Knife4j', '系统预置Knife4j文档账号',
        '9999-12-31 23:59:59', NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);


DROP TABLE IF EXISTS `base_role`;
CREATE TABLE `base_role` (
     `id` bigint(20) NOT NULL COMMENT 'id',
     `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
     `role_name_CN` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称中文',
     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
     `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
     `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
     PRIMARY KEY (`id`) USING BTREE,
     KEY `idx_base_role_role_name` (`role_name`),
     UNIQUE KEY `uq_role_role_name` (`role_name`,`deleted`) USING BTREE COMMENT '角色索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
INSERT INTO `base_role`
VALUES
(1, 'ROLE_ADMIN', '超级管理员', '系统预置', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
(2, 'ROLE_USER', '普通用户', '系统预置', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);


DROP TABLE IF EXISTS `base_user_role`;
CREATE TABLE `base_user_role`
(
    `id`      bigint(20) NOT NULL COMMENT 'id',
    `user_id` bigint(20) NOT NULL COMMENT '用户基本信息表id',
    `role_id` bigint(20) NOT NULL COMMENT '角色表id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_base_user_role_user_id` (`user_id`),
    KEY `idx_base_user_role_role_id` (`role_id`),
    CONSTRAINT `fk_base_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`),
    CONSTRAINT `fk_base_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '用户角色映射表';
INSERT INTO `base_user_role`
VALUES (1, 1, 1),(2, 2, 1);

DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu` (
     `id` bigint(20) NOT NULL COMMENT 'id',
     `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单id',
     `metadata` json NOT NULL COMMENT '前端菜单组件的信息',
     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
     `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
     `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

DROP TABLE IF EXISTS `base_role_menu`;
CREATE TABLE `base_role_menu`
(
    `id`      bigint(20) NOT NULL COMMENT 'id',
    `role_id` bigint(20) NOT NULL COMMENT '角色表id',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单表id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_base_role_menu_role_id` (`role_id`),
    KEY `idx_base_role_menu_menu_id` (`menu_id`),
    CONSTRAINT `fk_base_role_menu_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`),
    CONSTRAINT `fk_base_role_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `base_menu` (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '角色菜单映射表';