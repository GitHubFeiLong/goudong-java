ALTER TABLE `base_menu`
    ADD COLUMN `hide` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是隐藏菜单' AFTER `remark`;