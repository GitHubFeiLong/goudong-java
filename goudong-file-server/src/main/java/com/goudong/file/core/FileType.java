package com.goudong.file.core;

import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.file.exception.FileUploadException;
import com.goudong.file.util.FileUtils;
import lombok.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

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

    public FileType(FileTypeEnum type, Long length) {
        this.type = type;
        this.length = length;
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

    /**
     * 检查指定{@code file}是否满足上传配置
     * @param file
     */
    public void checkFileSize(MultipartFile file) {
        checkFileSize(file.getSize());
    }

    /**
     * 检查指定{@code fileSize}是否满足上传配置
     * @param fileSize
     */
    public void checkFileSize(long fileSize) {
        // 计算用户配置该类型文件允许上传的字节大小
        long bytes = this.fileLengthUnit.toBytes(this.length);
        if (bytes < fileSize) {
            ImmutablePair<Long, FileLengthUnit> var1 = FileUtils.adaptiveSize(fileSize);
            String message = String.format("文件(类型:%s,size:%s%s)超过配置(%s%s)",
                    this.type.name(),
                    var1.getLeft(),
                    var1.getRight(),
                    this.length,
                    this.fileLengthUnit);

            throw new FileUploadException(ClientExceptionEnum.BAD_REQUEST, message);
        }
    }
}
