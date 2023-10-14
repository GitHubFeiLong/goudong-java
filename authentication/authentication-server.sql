/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 127.0.0.1:3306
 Source Schema         : authentication-server

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 03/09/2023 20:21:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_app
-- ----------------------------
DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `secret`             varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '应用密钥',
    `name`               varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '应用名称',
    `home_page`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '首页',
    `enabled`            bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否激活',
    `remark`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL     DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL     DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_name` (`name`) USING BTREE COMMENT '应用名称唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1687091288592752641
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '应用表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_app
-- ----------------------------
INSERT INTO `base_app`
VALUES (1, '0b32758851f04e92bc0f874a4f82c4c2', 'admin', 'http://192.168.31.136:9999/#/login-success', b'1',
        '认证服务应用', NULL, NULL, NULL, NULL);
INSERT INTO `base_app`
VALUES (1687091288592752640, 'aaf0fd93ca774621b4d0139e2d2fa0c3', '4', NULL, b'1', '', '2023-08-03 21:21:23',
        '2023-08-03 21:21:23', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                              NOT NULL COMMENT '应用id',
    `parent_id`          bigint(20)                                              NULL DEFAULT NULL COMMENT '父级主键id',
    `permission_id`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '权限标识',
    `name`               varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '菜单名称',
    `type`               int(11)                                                 NOT NULL COMMENT '菜单类型（1：菜单；2：按钮；3：接口）',
    `path`               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由或接口地址',
    `method`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方式',
    `sort_num`           int(11)                                                 NULL DEFAULT NULL COMMENT '排序字段（值越小越靠前，仅仅针对前端路由）',
    `hide`               bit(1)                                                  NOT NULL COMMENT '是否是隐藏菜单',
    `meta`               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端菜单元数据',
    `remark`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1676562074974236673
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_menu
-- ----------------------------
INSERT INTO `base_menu`
VALUES (1667829212070445056, 1, NULL, 'sys', '系统管理', 1, '/user', NULL, 1, b'0',
        '{\"icon\": \"el-icon-s-tools\", \"title\": \"系统管理\"}', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212074639360, 1, 1667829212070445056, 'sys:user', '用户管理', 1, '/user/index', NULL, 2, b'0',
        '{\"icon\": \"peoples\", \"title\": \"用户管理\"}', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212074639361, 1, 1667829212074639360, 'sys:user:query', '查询用户', 2, '/api/user/base-user/page',
        '[\"GET\"]', 3, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212074639362, 1, 1667829212074639360, 'sys:user:add', '新增用户', 2,
        '/api/user/base-user/simple-create-user', '[\"POST\"]', 4, b'0', '{\"name\": \"cfl\"}', NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO `base_menu`
VALUES (1667829212074639364, 1, 1667829212074639360, 'sys:user:edit', '编辑用户', 2, '/api/user/base-user/admin/user',
        '[\"PUT\"]', 6, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212074639365, 1, 1667829212074639360, 'sys:user:delete', '删除用户', 2, '/api/user/base-user/*',
        '[\"DELETE\"]', 7, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833664, 1, 1667829212070445056, 'sys:role', '角色管理', 1, '/role/index', NULL, 8, b'0',
        '{\"icon\": \"iconfont-jueseguanli\", \"title\": \"角色管理\"}', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833665, 1, 1667829212078833664, 'sys:role:query', '查询角色', 2, '/api/user/base-role/page',
        '[\"GET\"]', 9, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833666, 1, 1667829212078833664, 'sys:role:add', '新增角色', 2, '/api/user/base-role', '[\"POST\"]',
        10, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833667, 1, 1667829212078833664, 'sys:role:edit', '编辑角色', 2, '/api/user/base-role', '[\"PUT\"]',
        11, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833668, 1, 1667829212078833664, 'sys:role:delete', '删除角色', 2, '/api/user/base-role/*',
        '[\"DELETE\"]', 12, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833669, 1, 1667829212078833664, 'sys:role:permission', '分配权限', 2,
        '/api/user/base-role/permissions/*', '[\"POST\"]', 13, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833670, 1, 1667829212070445056, 'sys:menu', '菜单管理', 1, '/menu/index', NULL, 14, b'0',
        '{\"icon\": \"iconfont-caidanguanli\", \"title\": \"菜单管理\"}', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212078833671, 1, 1667829212078833670, 'sys:menu:query', '查询菜单', 2, '/api/user/base-menu/tree',
        '[\"GET\"]', 15, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1667829212087222272, 1, 1667829212070445056, 'sys:app', '应用管理', 1, '/app/index', NULL, 16, b'0',
        '{\"icon\": \"iconfont-yingyongguanli\", \"title\": \"应用管理\"}', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1675371942527033344, 1, 1667829212074639360, 'sys:user:reset-password', '重置密码', 2,
        '/api/user/base-user/reset-password/*', '[\"PUT\"]', 7, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1675371942527033345, 1, 1667829212078833670, 'sys:menu:init', '初始菜单', 2, '/api/user/base-menu/init',
        '[\"POST\"]', 15, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1675371942531227648, 1, 1667829212087222272, 'sys:app:query', '查询应用', 2,
        '/api/authentication-server/app/page/base-app', '[\"POST\"]', 18, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1675371942531227649, 1, 1667829212087222272, 'sys:app:apply', '申请应用', 2,
        '/api/authentication-server/base-app', '[\"POST\"]', 19, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1675371942531227650, 1, 1667829212087222272, 'sys:app:audit', '审核应用', 2, '/api/oauth2/base-app/audit',
        '[\"PUT\"]', 20, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1675371942531227651, 1, 1667829212087222272, 'sys:app:delete', '删除应用', 2, '/api/oauth2/base-app/*',
        '[\"DELETE\"]', 21, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1676554953243148288, 1, 1667829212074639360, 'sys:user:enable', '激活用户', 2,
        '/api/user/base-user/change-enabled/*', '[\"PUT\"]', 7, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1676554953243148289, 1, 1667829212074639360, 'sys:user:lock', '锁定用户', 2,
        '/api/user/base-user/change-locked/*', '[\"PUT\"]', 8, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1676554953251536896, 1, 1667829212078833670, 'sys:menu:add', '新增菜单', 2, '/api/user/base-menu', '[\"POST\"]',
        19, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1676554953251536897, 1, 1667829212078833670, 'sys:menu:edit', '编辑菜单', 2, '/api/user/base-menu', '[\"PUT\"]',
        20, b'0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `base_menu`
VALUES (1676554953251536898, 1, 1667829212078833670, 'sys:menu:delete', '删除菜单', 2, '/api/user/base-menu/*',
        '[\"DELETE\"]', 21, b'0', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_role
-- ----------------------------
DROP TABLE IF EXISTS `base_role`;
CREATE TABLE `base_role`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                              NOT NULL COMMENT '应用id',
    `name`               varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '角色名称',
    `remark`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_role_app_id_name` (`app_id`, `name`) USING BTREE COMMENT '角色应用下唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1683401958217322497
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_role
-- ----------------------------
INSERT INTO `base_role`
VALUES (1, 1, 'ROLE_APP_SUPER_ADMIN', '所有应用的超级管理员', NULL, NULL, NULL, NULL);
INSERT INTO `base_role`
VALUES (2, 1, 'ROLE_APP_ADMIN', '应用管理员', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_role_menu`;
CREATE TABLE `base_role_menu`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `role_id`            bigint(20)                                              NULL DEFAULT NULL,
    `menu_id`            bigint(20)                                              NULL DEFAULT NULL,
    `created_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `created_date`       datetime(6)                                             NULL DEFAULT NULL,
    `last_modified_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `last_modified_date` datetime(6)                                             NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_base_role_menu_role_id` (`role_id`) USING BTREE,
    INDEX `fk_base_role_menu_menu_id` (`menu_id`) USING BTREE,
    CONSTRAINT `fk_base_role_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `base_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_base_role_menu_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '菜单角色中间表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_role_menu
-- ----------------------------
INSERT INTO `base_role_menu`
VALUES (13, 1, 1667829212070445056, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (14, 1, 1667829212070445056, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (15, 1, 1667829212074639360, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (16, 1, 1667829212074639361, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (17, 1, 1667829212074639362, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (18, 1, 1667829212074639364, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (19, 1, 1667829212074639365, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (20, 1, 1667829212078833664, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (21, 1, 1667829212078833665, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (22, 1, 1667829212078833666, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (23, 1, 1667829212078833667, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (24, 1, 1667829212078833668, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (25, 1, 1667829212078833669, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (26, 1, 1667829212078833670, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (27, 1, 1667829212078833671, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (28, 1, 1667829212087222272, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (29, 1, 1675371942527033344, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (30, 1, 1675371942527033345, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (31, 1, 1675371942531227648, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (32, 1, 1675371942531227649, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (33, 1, 1675371942531227650, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (34, 1, 1675371942531227651, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (35, 1, 1676554953243148288, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (36, 1, 1676554953243148289, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (37, 1, 1676554953251536896, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (38, 1, 1676554953251536897, NULL, NULL, NULL, NULL);
INSERT INTO `base_role_menu`
VALUES (39, 1, 1676554953251536898, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                              NOT NULL COMMENT '应用id',
    `real_app_id`        bigint(20)                                              NOT NULL COMMENT '真实应用id（例如xx应用管理员，app_id是认证服务应用的app_id，但是real_app_id是自己所管理xx应用的app_id）',
    `username`           varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '用户名',
    `password`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '密码',
    `valid_time`         datetime(6)                                             NOT NULL COMMENT '过期时间，不填永久有效',
    `enabled`            bit(1)                                                  NOT NULL COMMENT '激活状态：true 激活；false 未激活',
    `locked`             bit(1)                                                  NOT NULL COMMENT '锁定状态：true 锁定；false 未锁定',
    `remark`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_id_username` (`app_id`, `username`) USING BTREE COMMENT '用户名应用下唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1687091289242869761
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '基础用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user`
VALUES (1, 1, 1, 'admin', '$2a$10$G6ZOnXyHRuHM2eukWyrW6.sMMrtNZDl4URljrWR23EhvMOXY6JTWq', '2099-12-31 00:00:00.000000',
        b'1', b'0', '认证服务应用管理员', NULL, NULL, NULL, NULL);
INSERT INTO `base_user`
VALUES (1687091289242869760, 1, 1687091288592752640, '4',
        '$2a$10$JNlFik/nPc8LErWEeOQ3wOxk7Z4JQgdLIScuCiai7c.tZ.j1mR.YO', '2099-12-31 00:00:00.000000', b'1', b'0',
        '应用管理员', '2023-08-03 21:21:23', '2023-08-03 21:21:23', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_user_role
-- ----------------------------
DROP TABLE IF EXISTS `base_user_role`;
CREATE TABLE `base_user_role`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `user_id`            bigint(20)                                              NULL DEFAULT NULL,
    `role_id`            bigint(20)                                              NULL DEFAULT NULL,
    `created_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `created_date`       datetime(6)                                             NULL DEFAULT NULL,
    `last_modified_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `last_modified_date` datetime(6)                                             NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_base_user_role_user_id` (`user_id`) USING BTREE,
    INDEX `fk_base_user_role_role_id` (`role_id`) USING BTREE,
    CONSTRAINT `fk_base_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_base_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户角色中间表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user_role
-- ----------------------------
INSERT INTO `base_user_role`
VALUES (1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `base_user_role`
VALUES (11, 1687091289242869760, 2, NULL, NULL, NULL, NULL);

CREATE TABLE `base_app_cert`
(
    `id`                 bigint(20)                                               NOT NULL,
    `app_id`             bigint(20)                                               NOT NULL COMMENT '应用id',
    `serial_number`      varchar(16)                                              NOT NULL COMMENT '证书序号',
    `cert`               varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '证书',
    `private_key`        varchar(2048)                                            NOT NULL COMMENT '私钥',
    `public_key`         varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公钥',
    `valid_time`         datetime                                                 NOT NULL COMMENT '有效期截止时间',
    `remark`             varchar(255) DEFAULT NULL COMMENT '备注',
    `created_date`       datetime     DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime     DEFAULT NULL COMMENT '修改时间',
    `created_by`         varchar(255) DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_id_serial_number` (`app_id`, `serial_number`) USING BTREE COMMENT '应用证书唯一',
    CONSTRAINT `fx_app_id` FOREIGN KEY (`app_id`) REFERENCES `base_app` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='应用证书密钥信息';

SET FOREIGN_KEY_CHECKS = 1;
