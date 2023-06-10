drop table if exists `base_app`;
drop table if exists `base_app_open_user`;
CREATE TABLE `base_app`
(
    `id`             bigint(20)                                             NOT NULL AUTO_INCREMENT,
    `app_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'appId',
    `app_secret`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'appSecret',
    `app_name`       varchar(32)                                            NOT NULL COMMENT '应用名',
    `status`         tinyint(4)                                             NOT NULL COMMENT '状态（0：待审核；1：通过；2：拒绝；）',
    `remark`         varchar(255)                                                    DEFAULT NULL COMMENT '备注',
    `deleted`        bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否被删除',
    `create_time`    datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user_id` bigint(20)                                             NOT NULL DEFAULT '1' COMMENT '创建人id',
    `update_user_id` bigint(20)                                             NOT NULL DEFAULT '1' COMMENT '更新人id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_name` (`app_name`) USING BTREE COMMENT '应用名称唯一'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='三方应用信息';

CREATE TABLE `base_app_open_user`
(
    `id`             bigint(20)                                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `open_id`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'openId',
    `user_id`        bigint(20)                                             NOT NULL COMMENT '用户id',
    `deleted`        bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否被删除',
    `create_time`    datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user_id` bigint(20)                                             NOT NULL DEFAULT '1' COMMENT '创建人id',
    `update_user_id` bigint(20)                                             NOT NULL DEFAULT '1' COMMENT '更新人id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_id_open_id` (`app_id`, `open_id`) USING BTREE COMMENT '同一个应用下openId唯一'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='app下用户';