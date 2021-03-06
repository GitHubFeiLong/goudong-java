package com.goudong.file.util;

import cn.hutool.core.util.IdUtil;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.file.core.Filename;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.time.LocalDateTime;

import static com.goudong.commons.enumerate.file.FileLengthUnit.*;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/9 20:15
 */
public class FileUtils {
    private FileUtils(){

    }

    /**
     * 自适应文件大小和单位
     * @param fileSize
     * @return left是长度，right是单位
     */
    public static ImmutablePair<Long, FileLengthUnit> adaptiveSize(long fileSize) {
        // bit
        if (fileSize < C1) {
            return ImmutablePair.of(BYTE.toBits(fileSize), BIT);
        }
        // byte
        if (fileSize < C2/C1) {
            return ImmutablePair.of(BYTE.toBytes(fileSize), BYTE);
        }
        // kb
        if (fileSize < C3/C1) {
            return ImmutablePair.of(BYTE.toKbs(fileSize), KB);
        }
        // mb
        if (fileSize < C4/C1) {
            return ImmutablePair.of(BYTE.toMbs(fileSize), MB);
        }
        // gb
        if (fileSize < C5/C1) {
            return ImmutablePair.of(BYTE.toGbs(fileSize), GB);
        }
        // tb
        return ImmutablePair.of(BYTE.toTbs(fileSize), TB);
    }

    /**
     * 根据文件名进行构造对象
     *
     * @param originalFilename 文件名
     * @return
     */
    public static Filename getFilename(@NotBlank String originalFilename) {

        int index = originalFilename.lastIndexOf(".");
        String filename;
        String suffix;
        FileTypeEnum fileTypeEnum;
        switch (index) {
            case -1:
                return new Filename(originalFilename, null, null);
            case 0:
                suffix = originalFilename.substring(1);
                fileTypeEnum = FileTypeEnum.valueOf(suffix.toUpperCase());
                return new Filename(null, suffix, fileTypeEnum);
            default:
                filename = originalFilename.substring(0, index);
                suffix = originalFilename.substring(index + 1);
                fileTypeEnum = FileTypeEnum.valueOf(suffix.toUpperCase());
                return new Filename(filename, suffix, fileTypeEnum);
        }
    }

    public static File getFileDir(String rootDir) {
        String dateDir = LocalDateTime.now().toLocalDate().toString();
        return new File(rootDir + File.separator + dateDir);
    }

    /**
     * 获取文件存储的父目录名
     * @return yyyy-mm-dd
     */
    private static String getDateDir() {
        return LocalDateTime.now().toLocalDate().toString();
    }

    public static void main(String[] args) {
        String s = IdUtil.simpleUUID();
        System.out.println("s = " + s);
    }
}
