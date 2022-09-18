ALTER TABLE `base_menu`
    ADD COLUMN `sys` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是系统菜单（true：是；false：不是）' AFTER `api`;