package com.goudong.file.constant;

import java.io.File;

/**
 * 类描述：
 * file服务的常量类, 注意下面都没有加文件夹前后 斜杠
 * @author msi
 * @version 1.0
 * @date 2021/12/6 22:31
 */
public class FileConst {
    /**
     * 临时存储文件目录
     */
    public static final String TEMP_DIR = "temp";

    /**
     * 分片上传的临时文件存储
     */
    public static final String SHARD_TEMP_DIR = FileConst.TEMP_DIR + File.separator + "shard";

    /**
     * 使用Excel等文件进行数据导入时，如果文件内容过多，将文件存放的临时目录。
     * ex:/temp/import
     */
    public static final String IMPORT_TEMP_DIR = FileConst.TEMP_DIR + File.separator + "import";
}
