ALTER TABLE `base_menu`
    ADD COLUMN `component_path` varchar(255) NULL COMMENT '前端路由指向的组件地址' AFTER `path`;