/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 127.0.0.1:3306
 Source Schema         : goudong_user

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 24/06/2023 19:06:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_api_resource
-- ----------------------------
DROP TABLE IF EXISTS `base_api_resource`;
CREATE TABLE `base_api_resource`
(
    `id`               bigint(20)                                                    NOT NULL COMMENT 'id',
    `app_id`           bigint(20)                                                    NOT NULL COMMENT '应用id',
    `pattern`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路径Pattern',
    `method`           varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL DEFAULT 'GET' COMMENT '请求方式',
    `application_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'goudong-xxxx-server' COMMENT '接口所在应用',
    `remark`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
    `deleted`          bit(1)                                                        NULL     DEFAULT b'0' COMMENT '是否被删除',
    `update_time`      datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_time`      datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `create_user_id`   bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id`   bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_api_resource_pattern_method_application_name` (`pattern`, `method`, `application_name`) USING BTREE COMMENT '唯一索引',
    INDEX `idx_base_api_resource_application_name` (`application_name`) USING BTREE COMMENT '普通索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '保存系统中所有api接口资源'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_api_resource
-- ----------------------------

-- ----------------------------
-- Table structure for base_app
-- ----------------------------
DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app`
(
    `id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `app_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci        NOT NULL COMMENT 'appId',
    `app_secret`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci        NOT NULL COMMENT 'appSecret',
    `app_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '应用名',
    `status`         tinyint(4)                                                    NOT NULL COMMENT '状态（0：待审核；1：通过；2：拒绝；）',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注',
    `deleted`        bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否被删除',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_name` (`app_name`) USING BTREE COMMENT '应用名称唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1668248971442327564
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '三方应用信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_app
-- ----------------------------
INSERT INTO `base_app`
VALUES (1667779450730426368, 'gd$1$aMTY2Nzc3OTQ1MDczMDQyNjM2OA', 'ad6f121d48f540fdb7efe7ea80b40926', 'admin', 1, '后台管理',
        b'0', '2023-06-11 14:23:05', '2023-06-23 14:24:25', 1, 1);

-- ----------------------------
-- Table structure for base_app_open_user
-- ----------------------------
DROP TABLE IF EXISTS `base_app_open_user`;
CREATE TABLE `base_app_open_user`
(
    `id`             bigint(20)                                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `open_id`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'openId',
    `user_id`        bigint(20)                                             NOT NULL COMMENT '用户id',
    `deleted`        bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否被删除',
    `create_time`    datetime(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_user_id` bigint(20)                                             NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                             NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_id_open_id` (`app_id`, `open_id`) USING BTREE COMMENT '同一个应用下openId唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = 'app下用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_app_open_user
-- ----------------------------

-- ----------------------------
-- Table structure for base_authentication_log
-- ----------------------------
DROP TABLE IF EXISTS `base_authentication_log`;
CREATE TABLE `base_authentication_log`
(
    `id`             bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT 'id',
    `app_id`         bigint(20)                                              NOT NULL COMMENT '应用id',
    `principal`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '认证的主要信息（用户名，电话，邮箱）',
    `ip`             int(10) UNSIGNED                                        NULL     DEFAULT NULL COMMENT 'ip地址,使用整数存储，当获取不到ip时使用0',
    `ipv4`           varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT 'ipv4',
    `successful`     bit(1)                                                  NOT NULL DEFAULT b'1' COMMENT '是否是认证成功',
    `type`           varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT 'system' COMMENT '认证类型（system:系统用户,qq:qq快捷登录，wei_xin：微信快捷登录）',
    `description`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '描述，失败时记录失败原因',
    `deleted`        bit(1)                                                  NULL     DEFAULT b'0' COMMENT '是否被删除',
    `create_time`    datetime(0)                                             NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                             NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_user_id` bigint(20)                                              NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                              NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1672534127237824513
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户请求认证接口的日志，根据类型区分，系统用户，qq，微信等。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_authentication_log
-- ----------------------------

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu`
(
    `id`             bigint(20)                                                    NOT NULL COMMENT 'id',
    `app_id`         bigint(20)                                                    NOT NULL COMMENT '应用id',
    `parent_id`      bigint(20)                                                    NULL     DEFAULT NULL COMMENT '父菜单id',
    `name`           varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '菜单名',
    `type`           tinyint(4)                                                    NOT NULL DEFAULT 0 COMMENT '菜单类型（0：接口；1：菜单；2：按钮）',
    `open_model`     tinyint(4)                                                    NOT NULL DEFAULT 0 COMMENT '打开方式（0：内链；1：外链）',
    `path`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '前端路由或接口地址',
    `component_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '前端路由指向的组件地址',
    `method`         json                                                          NULL COMMENT '接口的请求方式',
    `icon`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '图标',
    `permission_id`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '权限标识（前端的菜单和按钮需要）',
    `sort_num`       int(11)                                                       NOT NULL DEFAULT 0 COMMENT '排序字段（值越小越靠前，仅仅针对前端路由）',
    `hide`           bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否是隐藏菜单',
    `metadata`       json                                                          NULL COMMENT '前端菜单组件的信息',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注',
    `deleted`        bit(1)                                                        NULL     DEFAULT b'0' COMMENT '是否被删除',
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `create_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_menu_permission_id` (`app_id`, `permission_id`) USING BTREE COMMENT '标识唯一'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_menu
-- ----------------------------
INSERT INTO `base_menu`
VALUES (1667829212070445056, 1667779450730426368, NULL, '系统管理', 1, 0, '/user', NULL, NULL, NULL, 'sys', 1, b'0', '{
  \"icon\": \"el-icon-s-tools\",
  \"title\": \"系统管理\"
}', NULL, b'0', '2023-06-24 12:51:11', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212074639360, 1667779450730426368, 1667829212070445056, '用户管理', 1, 0, '/user/index', NULL, NULL, NULL,
        'sys:user', 2, b'0', '{
    \"icon\": \"peoples\",
    \"title\": \"用户管理\"
  }', NULL, b'0', '2023-06-24 12:51:11', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212074639361, 1667779450730426368, 1667829212074639360, '查询用户', 2, 0, '/api/user/base-user/page', NULL,
        '[
          \"GET\"
        ]', NULL, 'sys:user:query', 3, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212074639362, 1667779450730426368, 1667829212074639360, '新增用户', 2, 0,
        '/api/user/base-user/simple-create-user', NULL, '[
    \"POST\"
  ]', NULL, 'sys:user:add', 4, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212074639363, 1667779450730426368, 1667829212074639360, '导出用户', 2, 0, '/api/user/base-user/export', NULL,
        '[
          \"GET\"
        ]', NULL, 'sys:user:export', 5, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212074639364, 1667779450730426368, 1667829212074639360, '编辑用户', 2, 0, '/api/user/base-user/admin/user',
        NULL, '[
    \"PUT\"
  ]', NULL, 'sys:user:edit', 6, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212074639365, 1667779450730426368, 1667829212074639360, '删除用户', 2, 0, '/api/user/base-user/*', NULL, '[
  \"DELETE\"
]', NULL, 'sys:user:delete', 7, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833664, 1667779450730426368, 1667829212070445056, '角色管理', 1, 0, '/role/index', NULL, NULL, NULL,
        'sys:role', 8, b'0', '{
    \"icon\": \"iconfont-jueseguanli\",
    \"title\": \"角色管理\"
  }', NULL, b'0', '2023-06-24 12:55:10', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833665, 1667779450730426368, 1667829212078833664, '查询角色', 2, 0, '/api/user/base-role/page', NULL,
        '[
          \"GET\"
        ]', NULL, 'sys:role:query', 9, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833666, 1667779450730426368, 1667829212078833664, '新增角色', 2, 0, '/api/user/base-role', NULL, '[
  \"POST\"
]', NULL, 'sys:role:add', 10, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833667, 1667779450730426368, 1667829212078833664, '编辑角色', 2, 0, '/api/user/base-role', NULL, '[
  \"PUT\"
]', NULL, 'sys:role:edit', 11, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833668, 1667779450730426368, 1667829212078833664, '删除角色', 2, 0, '/api/user/base-role/*', NULL, '[
  \"DELETE\"
]', NULL, 'sys:role:delete', 12, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833669, 1667779450730426368, 1667829212078833664, '分配权限', 2, 0,
        '/api/user/base-role/permissions/*', NULL, '[
    \"POST\"
  ]', NULL, 'sys:role:permission', 13, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833670, 1667779450730426368, 1667829212070445056, '菜单管理', 1, 0, '/menu/index', NULL, NULL, NULL,
        'sys:menu', 14, b'0', '{
    \"icon\": \"iconfont-caidanguanli\",
    \"title\": \"菜单管理\"
  }', NULL, b'0', '2023-06-24 12:55:24', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212078833671, 1667779450730426368, 1667829212078833670, '查询菜单', 2, 0, '/api/user/base-menu/tree', NULL,
        '[
          \"GET\"
        ]', NULL, 'sys:menu:query', 15, b'0', NULL, NULL, b'0', '2023-06-11 17:40:46', '2023-06-11 17:40:46', 1, 1);
INSERT INTO `base_menu`
VALUES (1667829212087222272, 1667779450730426368, 1667829212070445056, '应用管理', 1, 0, '/app/index', NULL, NULL, NULL,
        'sys:app', 16, b'0', '{
    \"icon\": \"iconfont-yingyongguanli\",
    \"title\": \"应用管理\"
  }', NULL, b'0', '2023-06-24 12:55:32', '2023-06-11 17:40:46', 1, 1);

-- ----------------------------
-- Table structure for base_role
-- ----------------------------
DROP TABLE IF EXISTS `base_role`;
CREATE TABLE `base_role`
(
    `id`             bigint(20)                                                    NOT NULL COMMENT 'id',
    `app_id`         bigint(20)                                                    NOT NULL COMMENT '应用id，应用之间进行隔离',
    `role_name`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
    `role_name_CN`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '角色名称中文',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注',
    `deleted`        bit(1)                                                        NULL     DEFAULT b'0' COMMENT '是否被删除',
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `create_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_role_role_name` (`app_id`, `role_name`, `deleted`) USING BTREE COMMENT '角色索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_role
-- ----------------------------
INSERT INTO `base_role`
VALUES (1, 1667779450730426368, 'ROLE_SUPER_ADMIN', '超级管理员', '系统预置', b'0', '2023-06-21 19:28:03', '2023-04-15 16:50:49',
        1, 1);
INSERT INTO `base_role`
VALUES (2, 1667779450730426368, 'ROLE_ADMIN', '管理员', '系统预置', b'0', '2023-06-23 15:30:38', '2023-06-20 21:51:05', 1, 1);

-- ----------------------------
-- Table structure for base_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_role_menu`;
CREATE TABLE `base_role_menu`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色表id',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单表id',
    INDEX `idx_base_role_menu_role_id` (`role_id`) USING BTREE,
    INDEX `idx_base_role_menu_menu_id` (`menu_id`) USING BTREE,
    CONSTRAINT `fk_base_role_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `base_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_base_role_menu_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单映射表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_role_menu
-- ----------------------------
INSERT INTO `base_role_menu`
VALUES (1, 1667829212070445056);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212074639360);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212074639361);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212074639362);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212074639363);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212074639364);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212074639365);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833664);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833665);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833666);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833667);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833668);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833669);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833670);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212078833671);
INSERT INTO `base_role_menu`
VALUES (1, 1667829212087222272);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212070445056);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833670);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833671);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833664);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833666);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833668);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833667);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833669);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212078833665);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212074639360);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212074639362);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212074639365);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212074639364);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212074639363);
INSERT INTO `base_role_menu`
VALUES (2, 1667829212074639361);

-- ----------------------------
-- Table structure for base_token
-- ----------------------------
DROP TABLE IF EXISTS `base_token`;
CREATE TABLE `base_token`
(
    `id`              bigint(20)                                             NOT NULL AUTO_INCREMENT COMMENT 'id',
    `app_id`          bigint(20)                                             NOT NULL COMMENT '应用id',
    `access_token`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '短期token',
    `refresh_token`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '刷新token',
    `access_expires`  datetime(0)                                            NOT NULL COMMENT 'access_token有效截止时长，APP上允许在线久一点。',
    `refresh_expires` datetime(0)                                            NOT NULL COMMENT 'refresh_token有效截止时长',
    `user_id`         bigint(20)                                             NOT NULL COMMENT '用户id',
    `client_type`     varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端类型',
    `deleted`         bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否被删除',
    `create_time`     datetime(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`     datetime(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_user_id`  bigint(20)                                             NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id`  bigint(20)                                             NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_token_access_token` (`access_token`) USING BTREE COMMENT '访问令牌唯一',
    UNIQUE INDEX `uk_base_token_refresh_token` (`refresh_token`) USING BTREE COMMENT '刷新令牌唯一',
    INDEX `idx_base_token_access_token` (`access_token`) USING BTREE COMMENT '访问令牌索引',
    INDEX `idx_base_token_refresh_token` (`refresh_token`) USING BTREE COMMENT '刷新令牌索引',
    INDEX `idx_base_token_user_id_client_type` (`user_id`, `client_type`) USING BTREE COMMENT 'user_id,client_type联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1672534127082635265
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户token记录'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_token
-- ----------------------------

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user`
(
    `id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `app_id`         bigint(20)                                                    NOT NULL COMMENT '应用id，应用之间进行隔离',
    `username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名',
    `password`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
    `email`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
    `phone`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '手机号',
    `sex`            int(11)                                                       NULL     DEFAULT 0 COMMENT '性别（0：未知；1：男；2：女）',
    `nickname`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '昵称',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注',
    `valid_time`     datetime(0)                                                   NOT NULL DEFAULT '9999-12-31 23:59:59' COMMENT '有效截止时间',
    `avatar`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '头像',
    `enabled`        bit(1)                                                        NOT NULL COMMENT '激活状态（true：激活；false：未激活）',
    `locked`         bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '锁定状态（true：已锁定；false：未锁定）',
    `deleted`        bit(1)                                                        NULL     DEFAULT b'0' COMMENT '是否被删除',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `create_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_user_username` (`app_id`, `username`, `deleted`) USING BTREE COMMENT '用户,用户名唯一键',
    UNIQUE INDEX `uk_base_user_phone` (`app_id`, `phone`, `deleted`) USING BTREE COMMENT '用户，手机号唯一键',
    UNIQUE INDEX `uk_base_user_email` (`app_id`, `email`, `deleted`) USING BTREE COMMENT '用户，邮箱唯一键'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1672163572382892033
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户基本信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user`
VALUES (1, 1667779450730426368, 'admin', '$2a$10$DI2GDONVUKrDKcV4C2iAq.8OJ70J5qvOqbm1nA8EF6pQfikbnPdLu', '@admin',
        'admin_phone', 1, '超级管理员', '系统预置超级管理员', '9999-12-31 23:59:59', NULL, b'1', b'0', b'0', '2023-04-15 16:50:49',
        '2023-06-16 21:30:15', 1, 1);

-- ----------------------------
-- Table structure for base_user_role
-- ----------------------------
DROP TABLE IF EXISTS `base_user_role`;
CREATE TABLE `base_user_role`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户基本信息表id',
    `role_id` bigint(20) NOT NULL COMMENT '角色表id',
    INDEX `idx_base_user_role_user_id` (`user_id`) USING BTREE,
    INDEX `idx_base_user_role_role_id` (`role_id`) USING BTREE,
    CONSTRAINT `fk_base_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_base_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色映射表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user_role
-- ----------------------------
INSERT INTO `base_user_role`
VALUES (1, 1);

-- ----------------------------
-- Table structure for base_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `base_whitelist`;
CREATE TABLE `base_whitelist`
(
    `id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `pattern`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路径Pattern',
    `methods`        json                                                          NOT NULL COMMENT '请求方式json数组',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注',
    `is_system`      bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否是系统预置的',
    `is_inner`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否只能内部服务调用',
    `is_disable`     bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否关闭该白名单',
    `deleted`        bit(1)                                                        NULL     DEFAULT b'0' COMMENT '是否被删除',
    `update_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改日期',
    `create_time`    datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
    `create_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '创建人id',
    `update_user_id` bigint(20)                                                    NOT NULL DEFAULT 1 COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_whitelist_pattern_deleted` (`pattern`, `deleted`) USING BTREE COMMENT 'pattern唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1647418502304370689
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '不需要授权就能访问的资源'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_whitelist
-- ----------------------------
INSERT INTO `base_whitelist`
VALUES (1, '/**/*.html*', '[
  \"GET\"
]', 'html', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (2, '/**/*.css*', '[
  \"GET\"
]', 'css', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (3, '/**/*.js*', '[
  \"GET\"
]', 'js', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (4, '/**/*.ico*', '[
  \"GET\"
]', 'ico', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (5, '/**/swagger-resources*', '[
  \"GET\"
]', 'swagger资源', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (6, '/**/api-docs*', '[
  \"GET\"
]', 'swagger接口信息', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (7, '/druid/**', '[
  \"GET\",
  \"POST\"
]', 'druid的监控工具', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (8, '/actuator/**', '[
  \"GET\"
]', 'spring boot的监控工具', b'1', b'0', b'0', b'0', '2023-04-15 16:53:07', '2023-04-15 16:53:07', 1, 1);
INSERT INTO `base_whitelist`
VALUES (1647418502300176384, '/api/file/download-group/download', '[
  \"GET\"
]', '白名单', b'0', b'0', b'0', b'0', '2023-04-16 09:55:53', '2023-04-16 09:55:53', 0, 0);
INSERT INTO `base_whitelist`
VALUES (1647418502300176385, '/api/file/file-link/*', '[
  \"GET\"
]', '白名单', b'0', b'0', b'0', b'0', '2023-04-16 09:55:53', '2023-04-16 09:55:53', 0, 0);
INSERT INTO `base_whitelist`
VALUES (1647418502300176386, '/api/file/upload-group/upload', '[
  \"POST\"
]', '白名单', b'0', b'0', b'0', b'0', '2023-04-16 09:55:53', '2023-04-16 09:55:53', 0, 0);
INSERT INTO `base_whitelist`
VALUES (1647418502300176387, '/api/file/upload-group/shard-upload', '[
  \"POST\"
]', '白名单', b'0', b'0', b'0', b'0', '2023-04-16 09:55:53', '2023-04-16 09:55:53', 0, 0);
INSERT INTO `base_whitelist`
VALUES (1647418502304370688, '/api/file/upload-group/shard-prefix-check', '[
  \"POST\"
]', '白名单', b'0', b'0', b'0', b'0', '2023-04-16 09:55:53', '2023-04-16 09:55:53', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
