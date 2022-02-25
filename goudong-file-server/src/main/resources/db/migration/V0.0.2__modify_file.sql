delete from `file`;

ALTER TABLE `file`
    ADD COLUMN `file_md5` varchar(32) NOT NULL COMMENT '文件的md5值' AFTER `file_path`;