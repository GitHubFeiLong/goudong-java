ALTER TABLE `base_user`
    ADD COLUMN `enabled` bit(1) NOT NULL COMMENT '激活状态（true：激活；false：未激活）' AFTER `qq_open_id`;