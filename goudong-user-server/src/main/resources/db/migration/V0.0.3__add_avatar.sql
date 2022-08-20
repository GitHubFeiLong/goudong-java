-- 添加头像字段
ALTER TABLE `base_user`
    ADD COLUMN `avatar` varchar(255) NULL COMMENT '头像' AFTER `valid_time`;