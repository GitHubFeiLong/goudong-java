package com.goudong.file.po;

import com.goudong.commons.core.jpa.BasePO;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类描述：
 * 文件上传记录表
 * @author msi
 * @version 1.0
 * @date 2021/12/11 22:43
 */
@Data
@Entity
@Table(name = "file")
public class FilePO extends BasePO {

    /**
     * 原始文件名
     */
    @Length(max = 128)
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    /**
     * 现在文件名
     */
    @Length(max = 128)
    @Column(name = "current_filename", nullable = false)
    private String currentFilename;

    /**
     * 文件类型
     * @see FileTypeEnum
     */
    @Length(max = 8)
    @Column(name = "file_type", nullable = false)
    private String fileType;

    /**
     * 文件大小，单位字节
     */
    @Column(name = "size", nullable = false)
    private Long size;

    /**
     * 文件大小
     */
    @Column(name = "file_length", nullable = false)
    private Long fileLength;

    /**
     * 文件大小单位
     * @see FileLengthUnit
     */
    @Column(name = "file_length_unit", nullable = false)
    private String fileLengthUnit;

    /**
     * 文件访问地址
     */
    @Length(max = 255)
    @Column(name = "file_link", nullable = false)
    private String fileLink;

    /**
     * 文件磁盘地址
     */
    @Length(max = 255)
    @Column(name = "file_path", nullable = false)
    private String filePath;
}
