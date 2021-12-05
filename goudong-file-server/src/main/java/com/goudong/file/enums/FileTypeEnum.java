package com.goudong.file.enums;

import com.goudong.commons.enumerate.FileLengthUnit;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 枚举描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/4 22:21
 */
public enum FileTypeEnum {
    JPG(10L, FileLengthUnit.MB),
    JPEG(10L, FileLengthUnit.MB);

    private Long length;
    private FileLengthUnit fileLengthUnit;

    FileTypeEnum( Long length, FileLengthUnit fileLengthUnit) {
        this.length = length;
        this.fileLengthUnit = fileLengthUnit;
    }

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
