package com.goudong.file.core;

import com.goudong.commons.enumerate.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类描述：
 * 文件名，包含文件名相关属性
 * @Author e-Feilong.Chen
 * @Date 2021/12/10 8:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filename {
    /**
     * 文件名
     */
    private String filename;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 类型枚举
     */
    private FileTypeEnum fileTypeEnum;
}
