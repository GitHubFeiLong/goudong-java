package com.goudong.file.core;

import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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
    @NotNull
    private FileTypeEnum type;

    /**
     * 文件大小,默认10
     */
    @Min(value = 0)
    private Long length = 10L;

    /**
     * 文件大小单位,默认MB
     */
    private FileLengthUnit fileLengthUnit = FileLengthUnit.MB;

    /**
     * 是否启用该类文件上传，默认true
     */
    private Boolean enabled = true;

    public FileType() {
    }

    public FileType(FileTypeEnum type) {
        this.type = type;
    }

    public FileType(FileTypeEnum type, Long length, FileLengthUnit fileLengthUnit) {
        this.type = type;
        this.length = length;
        this.fileLengthUnit = fileLengthUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileType)) return false;
        FileType fileType = (FileType) o;
        return type == fileType.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
