ALTER TABLE `base_menu`
    ADD COLUMN `app_id` bigint NOT NULL COMMENT '应用id' AFTER `id`;