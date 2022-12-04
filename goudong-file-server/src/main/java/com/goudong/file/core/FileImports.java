package com.goudong.file.core;

import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.file.constant.FileConst;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 文件数据导入，例如excel导入数据到数据库
 * @author cfl
 * @version 1.0
 * @date 2022/11/24 19:44
 */
@Data
public class FileImports {
    //~fields
    //==================================================================================================================
    /**
     * 使用Excel等文件进行数据导入时，如果文件内容过多，将文件存放的临时目录。
     * ex:/temp/import
     */
    public static final String IMPORT_TEMP_DIR = FileConst.IMPORT_TEMP_DIR;

    //~常见office文件
    //==================================================================================================================
    @NestedConfigurationProperty
    private FileType xls = new FileType(FileTypeEnum.XLS);
    @NestedConfigurationProperty
    private FileType xlsx = new FileType(FileTypeEnum.XLSX);
    /**
     * 文件类型, 程序将用户配置和默认配置统一设置到属性中，方便后期使用。
     */
    private List<FileType> fileTypes = new ArrayList<>();
    //~methods
    //==================================================================================================================

}
