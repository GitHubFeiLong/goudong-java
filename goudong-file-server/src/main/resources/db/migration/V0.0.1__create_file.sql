SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
                        `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
                        `original_filename` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原文件名',
                        `current_filename` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '重新命名的文件名',
                        `file_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型，小写',
                        `size` bigint(20) unsigned NOT NULL COMMENT '文件实际大小，单位字节',
                        `file_length` bigint(20) unsigned NOT NULL COMMENT '文件指定单位后的大小',
                        `file_length_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '指定的单位，小写',
                        `file_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件网络地址',
                        `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件磁盘地址',
                        `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
                        `update_time` datetime(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
                        `create_time` datetime(6) NOT NULL COMMENT '创建时间',
                        `update_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新人id',
                        `create_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建人id',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文件上传表记录';

SET
FOREIGN_KEY_CHECKS = 1;