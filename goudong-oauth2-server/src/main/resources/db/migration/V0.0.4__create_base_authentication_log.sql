DROP TABLE IF EXISTS `base_authentication_log`;
CREATE TABLE `base_authentication_log` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `principal` varchar(64) NOT NULL COMMENT '认证的主要信息（用户名，电话，邮箱）',
   `ip` int(10) unsigned DEFAULT NULL COMMENT 'ip地址,使用整数存储，当获取不到ip时使用0',
   `successful` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否是认证成功',
   `type` varchar(16) NOT NULL DEFAULT 'system' COMMENT '认证类型（system:系统用户,qq:qq快捷登录，wei_xin：微信快捷登录）',
   `description` varchar(255) DEFAULT NULL COMMENT '描述，失败时记录失败原因',
   `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
   `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户请求认证接口的日志，根据类型区分，系统用户，qq，微信等。';