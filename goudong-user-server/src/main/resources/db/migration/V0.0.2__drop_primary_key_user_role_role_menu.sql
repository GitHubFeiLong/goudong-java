-- 删除中间表的主键
ALTER TABLE `base_user_role`
DROP COLUMN `id`,
DROP PRIMARY KEY;

ALTER TABLE `base_role_menu`
DROP COLUMN `id`,
DROP PRIMARY KEY;