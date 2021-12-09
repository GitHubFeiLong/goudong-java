package com.goudong.file.util;

import com.goudong.commons.enumerate.FileLengthUnit;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.time.LocalDateTime;

import static com.goudong.commons.enumerate.FileLengthUnit.*;

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
     * 获取文件上传的目录地址（指定目录下再创建日期文件夹）
     * @param rootDir 指定目录下
     * @return /rootDir/yyyy-mm-dd
     */
    public static String getDir (String rootDir) {
        String dateDir = LocalDateTime.now().toLocalDate().toString();
        return rootDir + File.separator + dateDir;
    }


    public static void main(String[] args) {
        ImmutablePair immutablePair = FileUtils.adaptiveSize(1024);
        System.out.println("immutablePair = " + immutablePair);
    }
}
