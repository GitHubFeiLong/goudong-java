package com.goudong.commons.enumerate;

import java.util.Set;
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
    public static Set<FileTypeEnum> listValues() {
        Set<FileTypeEnum> values = Stream.of(FileTypeEnum.values()).filter(f -> {
            return true;
        }).collect(Collectors.toSet());
        return values;
    }
}
