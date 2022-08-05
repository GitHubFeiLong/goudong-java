-- 修改白名单表结构，添加字段是否内部服务使用,是否关闭该白名单
ALTER TABLE `base_whitelist`
    ADD COLUMN `is_inner` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否只能内部服务调用' AFTER `is_system`;
ALTER TABLE `base_whitelist`
    ADD COLUMN `is_disable` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否关闭该白名单' AFTER `is_inner`;