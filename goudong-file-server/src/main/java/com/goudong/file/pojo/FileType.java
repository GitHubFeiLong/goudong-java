package com.goudong.file.pojo;

import com.goudong.commons.enumerate.FileLengthUnit;
import com.goudong.commons.enumerate.FileTypeEnum;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * 类描述：
 * 文件类型
 * @author msi
 * @version 1.0
 * @date 2021/12/5 13:11
 */
@Data
@Validated
public class FileType {
    /**
     * 文件类型
     */
    private FileTypeEnum type;

    /**
     * 文件大小
     */
    @Min(value = 0)
    private Long length = 10L;

    /**
     * 文件大小单位
     */
    private FileLengthUnit fileLengthUnit = FileLengthUnit.MB;
}
