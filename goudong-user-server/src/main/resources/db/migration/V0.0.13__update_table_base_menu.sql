ALTER TABLE `base_menu`
    MODIFY COLUMN `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标' AFTER `method`;