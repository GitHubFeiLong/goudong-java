DROP TABLE IF EXISTS `authority_user`;
CREATE TABLE `authority_user`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
    `password`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
    `email`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
    `phone`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
    `nickname`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
    `remark`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `valid_time`     datetime(0) DEFAULT NULL COMMENT '有效截止时间',
    `qq_open_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账户关联qq的openId',
    `deleted`        tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被删除',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime(0) COMMENT '更新时间',
    `create_user_id` bigint(20) NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uq_user_username`(`username`) USING BTREE COMMENT '用户,用户名唯一键',
    UNIQUE INDEX `uq_user_phone`(`phone`) USING BTREE COMMENT '用户，手机号唯一键',
    UNIQUE INDEX `uq_user_email`(`email`) USING BTREE COMMENT '用户，邮箱唯一键'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = Dynamic;
INSERT INTO `authority_user`
VALUES (1, 'admin', '$2a$10$8ptteV1xP51AjOS/u6NAle/1Pw2BUsS.D/mbGuhauf.qc.oTikeAy', '@', '1', NULL, '超级管理员',
        NULL, NULL, 0, now(), NULL, 1, NULL);


DROP TABLE IF EXISTS `authority_role`;
CREATE TABLE `authority_role`
(
    `id`             bigint(20) NOT NULL COMMENT 'id',
    `role_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
    `role_name_CN`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称中文',
    `remark`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `deleted`        tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被删除',
    `update_time`    datetime(0) COMMENT '更新时间',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user_id` bigint(20) NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;
INSERT INTO `authority_role`
VALUES
(1, 'ROLE_ADMIN', '超级管理员', '', 0, NULL, now(), 1, NULL),
(2, 'ROLE_ORDINARY', '普通用户', '', 0, NULL, now(), 1, NULL);


DROP TABLE IF EXISTS `authority_user_role`;
CREATE TABLE `authority_user_role`
(
    `id`      bigint(20) NOT NULL COMMENT 'id',
    `user_id` bigint(20) NOT NULL COMMENT '用户基本信息表id',
    `role_id` bigint(20) NOT NULL COMMENT '角色表id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色映射表' ROW_FORMAT = Dynamic;
INSERT INTO `authority_user_role`
VALUES (1, 1, 1);


DROP TABLE IF EXISTS `authority_menu`;
CREATE TABLE `authority_menu`
(
    `id`             bigint(20) NOT NULL COMMENT 'id',
    `url`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求路径',
    `method`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '请求方式',
    `menu_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名称',
    `parent_id`      bigint(20) DEFAULT NULL COMMENT '父菜单id',
    `remark`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
    `deleted`        tinyint(1) DEFAULT 0 COMMENT '是否被删除',
    `update_time`    datetime(0) COMMENT '更新时间',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user_id` bigint(20) NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `authority_role_menu`;
CREATE TABLE `authority_role_menu`
(
    `id`      bigint(20) NOT NULL COMMENT 'id',
    `role_id` bigint(20) NOT NULL COMMENT '角色id',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单映射表' ROW_FORMAT = Dynamic;
