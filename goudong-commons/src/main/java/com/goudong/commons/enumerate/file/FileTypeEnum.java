package com.goudong.commons.enumerate.file;


import com.goudong.core.lang.IEnum;
import org.apache.commons.lang3.StringUtils;

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
public enum FileTypeEnum implements IEnum<String, FileTypeEnum> {
    //~常见图片
    //==================================================================================================================
    JPG,
    JPEG,
    PNG,
    GIF,
    SVG,

    //~常见office文件
    //==================================================================================================================
    XLS,
    XLSX,
    DOC,
    DOCX,
    PPT,
    PPTX,

    PDF,

    //~常见文本文档
    //==================================================================================================================
    TXT,
    MD,

    //~常见音频
    //==================================================================================================================
    MP3,

    //~常见视频
    //==================================================================================================================
    AVI,
    MOV,
    RMVB,
    RM,
    FLV,
    MP4,
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
        if (StringUtils.isBlank(fileType)) {
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

    /**
     * 获取枚举成员的唯一标识
     *
     * @return
     */
    @Override
    public String getId() {
        return this.name();
    }

    /**
     * 根据{@code id} 找到对应的枚举成员并返回<br>
     *
     * @param s 待找的枚举成员id
     * @return
     */
    @Override
    public FileTypeEnum getById(String s) {
        return IEnum.super.getById(s);
    }


}
