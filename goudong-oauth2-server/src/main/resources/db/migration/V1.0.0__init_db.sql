SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `goudong`.`authority_ignore_resource`;
DROP TABLE IF EXISTS `goudong`.`authority_menu`;
DROP TABLE IF EXISTS `goudong`.`authority_role`;
DROP TABLE IF EXISTS `goudong`.`authority_role_menu`;
DROP TABLE IF EXISTS `goudong`.`authority_user`;
DROP TABLE IF EXISTS `goudong`.`authority_user_role`;
DROP TABLE IF EXISTS `goudong`.`invalid_email`;
CREATE TABLE `authority_ignore_resource` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT 'uuid',
  `url` varchar(255) NOT NULL COMMENT '路径',
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求方式请求方式(多个用逗号分隔)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='不需要授权就能访问的资源';
CREATE TABLE `authority_menu` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT 'uuid',
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
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT 'uuid',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称(必须以ROLE_起始命名)',
  `role_name_CN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称中文',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';
CREATE TABLE `authority_role_menu` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT 'uuid',
  `role_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色uuid',
  `menu_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单uuid',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单映射表';
CREATE TABLE `authority_user` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT 'uuid',
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
  `qq_open_id` varchar(0) DEFAULT NULL COMMENT 'qq登录后，系统获取腾讯的open_id',
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE KEY `uq_user_username` (`username`) USING BTREE COMMENT '用户,用户名唯一键',
  UNIQUE KEY `uq_user_phone` (`phone`) USING BTREE COMMENT '用户，手机号唯一键',
  UNIQUE KEY `uq_user_email` (`email`) USING BTREE COMMENT '用户，邮箱唯一键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息表';
CREATE TABLE `authority_user_role` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT 'uuid',
  `user_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户基本信息表uuid',
  `role_uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色表uuid',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色映射表';
CREATE TABLE `invalid_email` (
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT (uuid()) COMMENT '主键',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '无效邮箱，当发送验证码时，报错就记录邮箱地址',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_invalidEmail_email` (`email`) USING BTREE COMMENT '失效邮箱，邮箱唯一键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='注册时，发送邮箱验证码，邮箱无效就记录到该表，再次发送验证码时先检索该表';

INSERT INTO `goudong`.`authority_ignore_resource` (`uuid`,`url`,`method`,`remark`,`is_delete`,`update_time`,`create_time`) VALUES ('1', '/swagger-ui.html', 'GET', 'swagger', 0, '2021-04-03 20:54:08', '2021-04-03 20:54:08'),('10', '/api/user/**', 'GET', NULL, 0, '2021-04-04 20:50:13', '2021-04-04 20:50:13'),('11', '/oauth/qq/**', 'GET', NULL, 0, '2021-05-01 21:54:21', '2021-05-01 21:54:21'),('2', '/swagger-ui/*', 'GET', NULL, 0, '2021-04-03 20:54:24', '2021-04-03 20:54:24'),('3', '/swagger-resources/**', 'GET', NULL, 0, '2021-04-03 20:54:40', '2021-04-03 20:54:40'),('4', '/druid/**', 'GET,POST', NULL, 0, '2021-04-04 11:24:23', '2021-04-04 11:24:23'),('5', '/v3/api-docs', 'GET', NULL, 0, '2021-04-03 20:55:01', '2021-04-03 20:55:01'),('6', '/actuator/**', 'GET', NULL, 0, '2021-04-04 15:21:56', '2021-04-04 15:21:56'),('7', '/**/*.css', 'GET', NULL, 0, '2021-04-04 15:35:22', '2021-04-04 15:35:22'),('8', '/**/*.ico', 'GET', NULL, 0, '2021-04-04 15:35:51', '2021-04-04 15:35:51'),('9', '/**/*.js', 'GET', NULL, 0, '2021-04-04 15:36:02', '2021-04-04 15:36:02');

INSERT INTO `goudong`.`authority_menu` (`uuid`,`url`,`method`,`menu_name`,`parent_uuid`,`remark`,`is_delete`,`update_time`,`create_time`) VALUES ('40cbd232-b1f8-11eb-b496-b42e99aedacc', '/api/user/hello', 'get', NULL, NULL, NULL, 0, '2021-04-03 17:52:07', '2021-04-03 17:52:07');

INSERT INTO `goudong`.`authority_role` (`uuid`,`role_name`,`role_name_CN`,`remark`,`is_delete`,`update_time`,`create_time`) VALUES ('2fe7f699-b1f6-11eb-b496-b42e99aedacc', 'ROLE_ADMIN', '管理员', '', 0, '2021-05-11 09:14:08', '2021-05-11 09:14:08'),('aa22cae8-b1f7-11eb-b496-b42e99aedacc', 'ROLE_ORDINARY', '普通角色', NULL, 0, '2021-05-11 09:24:43', '2021-05-11 09:24:43');

INSERT INTO `goudong`.`authority_role_menu` (`uuid`,`role_uuid`,`menu_uuid`,`is_delete`,`update_time`,`create_time`) VALUES ('2dd05b3d-b1f8-11eb-b496-b42e99aedacc', '2fe7f699-b1f6-11eb-b496-b42e99aedacc', '40cbd232-b1f8-11eb-b496-b42e99aedacc', 0, '2021-04-03 17:52:14', '2021-04-03 17:52:14');

INSERT INTO `goudong`.`authority_user` (`uuid`,`username`,`password`,`email`,`phone`,`nickname`,`remark`,`valid_time`,`is_delete`,`update_time`,`create_time`,`qq_open_id`) VALUES ('ed7bc254-b1f7-11eb-b496-b42e99aedacc', 'admin', '$2a$10$8ptteV1xP51AjOS/u6NAle/1Pw2BUsS.D/mbGuhauf.qc.oTikeAy', '1', '15213507716', NULL, NULL, '2021-05-08 15:51:25', 0, '2021-04-03 15:48:39', '2021-04-03 15:48:39', NULL);

INSERT INTO `goudong`.`authority_user_role` (`uuid`,`user_uuid`,`role_uuid`,`is_delete`,`update_time`,`create_time`) VALUES ('0a233801-b1f8-11eb-b496-b42e99aedacc', 'ed7bc254-b1f7-11eb-b496-b42e99aedacc', '2fe7f699-b1f6-11eb-b496-b42e99aedacc', 0, '2021-04-03 16:52:22', '2021-04-03 16:52:22');
