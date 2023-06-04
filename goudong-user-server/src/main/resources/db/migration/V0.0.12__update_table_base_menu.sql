SET FOREIGN_KEY_CHECKS=0;
truncate table `base_role_menu`;

drop table IF EXISTS  `base_menu`;

CREATE TABLE `base_menu` (
     `id` bigint(20) NOT NULL COMMENT 'id',
     `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单id',
     `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名',
     `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '菜单类型（0：接口；1：菜单；2：按钮）',
     `open_model` tinyint(4) NOT NULL DEFAULT '0' COMMENT '打开方式（0：内链；1：外链）',
     `path` varchar(255) NOT NULL COMMENT '前端路由或接口地址',
     `method` json DEFAULT NULL COMMENT '接口的请求方式',
     `icon` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
     `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识（前端的菜单和按钮需要）',
     `sort_num` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段（值越小越靠前，仅仅针对前端路由）',
     `hide` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是隐藏菜单',
     `metadata` json DEFAULT NULL COMMENT '前端菜单组件的信息',
     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
     `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `create_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人id',
     `update_user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人id',
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE KEY `uk_base_menu_permission_id` (`permission_id`) USING BTREE COMMENT '标识唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';


SET FOREIGN_KEY_CHECKS=1;