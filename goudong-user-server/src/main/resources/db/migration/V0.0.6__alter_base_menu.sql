-- 删除u非空约束
ALTER TABLE `base_menu`
    MODIFY COLUMN `metadata` json NULL COMMENT '前端菜单组件的信息' AFTER `method`;