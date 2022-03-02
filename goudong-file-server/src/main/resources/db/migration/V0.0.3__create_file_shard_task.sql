drop table if exists `file_shard_task`;
CREATE TABLE `file_shard_task` (
       `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
       `file_md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件的md5值',
       `shard_index` bigint(20) NOT NULL COMMENT '分片索引',
       `block_size` bigint(20) NOT NULL COMMENT '块大小',
       `temp_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分片临时文件存储的磁盘地址',
       `last_modified_time` datetime NOT NULL COMMENT '源文件最后修改时间',
       `successful` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否成功',
       `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
       `update_time` datetime(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
       `create_time` datetime(6) NOT NULL COMMENT '创建时间',
       `update_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新人id',
       `create_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建人id',
       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='分片上传文件的任务表';