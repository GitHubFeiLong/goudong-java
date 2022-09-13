-- 修改菜单表
ALTER TABLE `base_menu`
    ADD COLUMN `name` varchar(16) NULL COMMENT '菜单名' AFTER `parent_id`,
    ADD COLUMN `api` bit(1) NOT NULL COMMENT '是否是api（true：是api；false：是前端路由）' AFTER `name`,
    ADD COLUMN `path` varchar(255) NOT NULL COMMENT '前端路由或接口地址' AFTER `api`,
    ADD COLUMN `method` varchar(16) NULL COMMENT '接口的请求方式' AFTER `path`;