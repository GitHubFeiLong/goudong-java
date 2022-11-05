DROP TABLE IF EXISTS `base_whitelist`;
CREATE TABLE `base_whitelist` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `pattern` varchar(255) NOT NULL COMMENT '路径Pattern',
    `methods` json NOT NULL COMMENT '请求方式json数组',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `is_system` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是系统预置的',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
    `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_base_whitelist_pattern_deleted` (`pattern`,`deleted`) USING BTREE COMMENT 'pattern唯一'
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='不需要授权就能访问的资源';

INSERT INTO `base_whitelist` (`id`, `pattern`, `methods`, `remark`, `is_system`)
VALUES
(1, '/**/*.html*', '["GET"]', 'html', true),
(2, '/**/*.css*', '["GET"]', 'css', true),
(3, '/**/*.js*', '["GET"]', 'js' , true),
(4, '/**/*.ico*', '["GET"]', 'ico', true),
(5,'/**/swagger-resources*','["GET"]', 'swagger资源', true),
(6,'/**/api-docs*','["GET"]', 'swagger接口信息', true),
(7,'/druid/**','["GET","POST"]', 'druid的监控工具', true),
(8,'/actuator/**','["GET"]', 'spring boot的监控工具', true)
;
