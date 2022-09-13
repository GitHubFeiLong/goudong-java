-- 添加索引
ALTER TABLE `base_menu`
    ADD UNIQUE INDEX `uk_base_menu_path_method`(`path`, `method`, `deleted`) USING BTREE COMMENT '请求地址和请求方式唯一索引';