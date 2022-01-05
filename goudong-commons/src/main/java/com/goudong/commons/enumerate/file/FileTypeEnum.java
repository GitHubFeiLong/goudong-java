package com.goudong.commons.enumerate.file;

import jodd.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 枚举描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/5 18:41
 */
public enum FileTypeEnum {
    JPG,
    JPEG,
    PNG,
    GIF,
    SVG,

    XLS,
    XLSX,
    DOC,
    DOCX,
    PPT,
    PPTX,

    PDF,

    TXT,

    MD,
    ;

    /**
     * 获取有效值
     * @return
     */
    public static List<FileTypeEnum> listValues() {
        List<FileTypeEnum> values = Stream.of(FileTypeEnum.values()).filter(f -> {
            return true;
        }).collect(Collectors.toList());
        return values;
    }

    /**
     * 字符串转枚举
     * @param fileType 文件类型字符串
     * @return
     */
    public static FileTypeEnum convert(String fileType) {
        if (StringUtil.isBlank(fileType)) {
            throw new IllegalArgumentException("参数不能为空，转枚举时失败");
        }
        return FileTypeEnum.valueOf(fileType.toUpperCase());
    }

    /**
     * 使用类型作为后缀，拼接小数点和小写后缀名
     * @return
     */
    public String lowerName() {
        return this.name().toLowerCase();
    }
}
