DROP TABLE IF EXISTS `base_ignore_resource`;
CREATE TABLE `base_ignore_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pattern` varchar(255) NOT NULL COMMENT '路径Pattern',
  `method` varchar(255) NOT NULL COMMENT '请求方式请求方式(多个用逗号分隔)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_system` tinyint(1) DEFAULT 0 COMMENT '是否是系统预置的',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否被删除',
  `update_time` datetime(0) COMMENT '修改日期',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_base_ignore_resource__url_method`(`pattern`, `method`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '不需要授权就能访问的资源';

INSERT INTO `base_ignore_resource` ( `pattern`, `method`, `remark`, `is_system`,`create_user_id`)
VALUES
( '/**/*.html*', 'GET', 'html', true, 1),
( '/**/*.css*', 'GET', 'css', true, 1),
( '/**/*.js*', 'GET', 'js' , true, 1),
( '/**/*.ico*', 'GET', 'ico', true, 1),
('/**/swagger-resources*','GET', 'swagger资源', true, 1),
('/**/api-docs*','GET', 'swagger接口信息', true, 1),
('/druid/**','GET,POST', 'druid的监控工具', true, 1),
('/actuator/**','GET', 'spring boot的监控工具', true, 1)
;
