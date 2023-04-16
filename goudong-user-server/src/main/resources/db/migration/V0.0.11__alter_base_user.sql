ALTER TABLE `base_user`
    ADD COLUMN `locked` bit(1) NOT NULL COMMENT '锁定状态（true：已锁定；false：未锁定）' DEFAULT b'0' AFTER `enabled`,
    ADD COLUMN `sex` int NULL COMMENT '性别（0：未知；1：男；2：女）' default 0 AFTER `phone`;

update `base_user` set `locked`=false, sex=1;