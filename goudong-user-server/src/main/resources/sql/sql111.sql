/*
MySQL Backup
Database: security
Backup Time: 2021-04-05 21:14:46
*/

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `security`.`authority_ignore_resource`;
DROP TABLE IF EXISTS `security`.`authority_menu`;
DROP TABLE IF EXISTS `security`.`authority_role`;
DROP TABLE IF EXISTS `security`.`authority_role_menu`;
DROP TABLE IF EXISTS `security`.`authority_user`;
DROP TABLE IF EXISTS `security`.`authority_user_role`;
DROP TABLE IF EXISTS `security`.`flyway_schema_history`;
CREATE TABLE `authority_ignore_resource` (
  `uuid` varchar(255) NOT NULL COMMENT 'uuid',
  `url` varchar(255) NOT NULL COMMENT '路径',
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求方式请求方式(多个用逗号分隔)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='不需要授权就能访问的资源';
CREATE TABLE `authority_menu` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'uuid',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求路径',
  `method` varchar(255) NOT NULL COMMENT '请求方式',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名称',
  `parent_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父菜单uuid',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';
CREATE TABLE `authority_role` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'uuid',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
  `role_name_CN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称中文',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';
CREATE TABLE `authority_role_menu` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'uuid',
  `role_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色uuid',
  `menu_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单uuid',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单映射表';
CREATE TABLE `authority_user` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'uuid',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `valid_time` datetime DEFAULT NULL COMMENT '有效截止时间',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息表';
CREATE TABLE `authority_user_role` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'uuid',
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户基本信息表uuid',
  `role_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色表uuid',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色映射表';
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
BEGIN;
LOCK TABLES `security`.`authority_ignore_resource` WRITE;
DELETE FROM `security`.`authority_ignore_resource`;
INSERT INTO `security`.`authority_ignore_resource` (`uuid`,`url`,`method`,`remark`,`is_delete`,`update_time`,`create_time`) VALUES ('1', '/swagger-ui.html', 'GET', 'swagger', 0, '2021-04-03 20:54:08', '2021-04-03 20:54:08'),('10', '/api/user/**', 'GET', NULL, 0, '2021-04-04 20:50:13', '2021-04-04 20:50:13'),('2', '/swagger-ui/*', 'GET', NULL, 0, '2021-04-03 20:54:24', '2021-04-03 20:54:24'),('3', '/swagger-resources/**', 'GET', NULL, 0, '2021-04-03 20:54:40', '2021-04-03 20:54:40'),('4', '/druid/**', 'GET,POST', NULL, 0, '2021-04-04 11:24:23', '2021-04-04 11:24:23'),('5', '/v3/api-docs', 'GET', NULL, 0, '2021-04-03 20:55:01', '2021-04-03 20:55:01'),('6', '/actuator/**', 'GET', NULL, 0, '2021-04-04 15:21:56', '2021-04-04 15:21:56'),('7', '/**/*.css', 'GET', NULL, 0, '2021-04-04 15:35:22', '2021-04-04 15:35:22'),('8', '/**/*.ico', 'GET', NULL, 0, '2021-04-04 15:35:51', '2021-04-04 15:35:51'),('9', '/**/*.js', 'GET', NULL, 0, '2021-04-04 15:36:02', '2021-04-04 15:36:02');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `security`.`authority_menu` WRITE;
DELETE FROM `security`.`authority_menu`;
INSERT INTO `security`.`authority_menu` (`uuid`,`url`,`method`,`menu_name`,`parent_uuid`,`remark`,`is_delete`,`update_time`,`create_time`) VALUES ('1', '/api/user/hello', 'get', NULL, NULL, NULL, 0, '2021-04-03 17:52:07', '2021-04-03 17:52:07');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `security`.`authority_role` WRITE;
DELETE FROM `security`.`authority_role`;
INSERT INTO `security`.`authority_role` (`uuid`,`role_name`,`role_name_CN`,`remark`,`is_delete`,`update_time`,`create_time`) VALUES ('1', 'ROLE_ADMIN', '管理员', NULL, 0, '2021-04-03 16:52:11', '2021-04-03 16:52:11');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `security`.`authority_role_menu` WRITE;
DELETE FROM `security`.`authority_role_menu`;
INSERT INTO `security`.`authority_role_menu` (`uuid`,`role_uuid`,`menu_uuid`,`is_delete`,`update_time`,`create_time`) VALUES ('1', '1', '1', 0, '2021-04-03 17:52:14', '2021-04-03 17:52:14');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `security`.`authority_user` WRITE;
DELETE FROM `security`.`authority_user`;
INSERT INTO `security`.`authority_user` (`uuid`,`username`,`password`,`email`,`phone`,`nickname`,`remark`,`valid_time`,`is_delete`,`update_time`,`create_time`) VALUES ('1', 'admin', '$2a$10$8ptteV1xP51AjOS/u6NAle/1Pw2BUsS.D/mbGuhauf.qc.oTikeAy', '1', '1', NULL, NULL, '2021-05-08 15:51:25', 0, '2021-04-03 15:48:39', '2021-04-03 15:48:39');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `security`.`authority_user_role` WRITE;
DELETE FROM `security`.`authority_user_role`;
INSERT INTO `security`.`authority_user_role` (`uuid`,`user_uuid`,`role_uuid`,`is_delete`,`update_time`,`create_time`) VALUES ('1', '1', '1', 0, '2021-04-03 16:52:22', '2021-04-03 16:52:22');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `security`.`flyway_schema_history` WRITE;
DELETE FROM `security`.`flyway_schema_history`;
INSERT INTO `security`.`flyway_schema_history` (`installed_rank`,`version`,`description`,`type`,`script`,`checksum`,`installed_by`,`installed_on`,`execution_time`,`success`) VALUES (1, '1.0.0', 'init', 'SQL', 'V1.0.0__init.sql', 1985037391, 'root', '2021-04-02 21:20:20', 350, 1);
UNLOCK TABLES;
COMMIT;
