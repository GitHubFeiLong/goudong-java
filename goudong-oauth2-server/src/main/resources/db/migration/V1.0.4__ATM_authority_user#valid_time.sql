update `goudong`.`authority_user` set `valid_time` = '9999-12-31 00:00:00' where `valid_time` is null;
ALTER TABLE `goudong`.`authority_user`
    MODIFY COLUMN `valid_time` datetime(0) NOT NULL DEFAULT '9999-12-31 00:00:00' COMMENT '有效截止时间' AFTER `remark`;