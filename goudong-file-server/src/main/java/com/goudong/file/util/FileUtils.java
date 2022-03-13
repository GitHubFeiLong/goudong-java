package com.goudong.file.util;

import cn.hutool.core.io.FileUtil;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.constant.FileConst;
import com.goudong.file.core.Filename;
import com.goudong.file.po.FilePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static com.goudong.commons.enumerate.file.FileLengthUnit.*;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/9 20:15
 */
@Slf4j
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
     * @param customerFilename 客户端自定义文件名
     * @param originalFilename 上传文件的文件名
     * @return
     */
    public static Filename getFilename(String customerFilename, @NotBlank String originalFilename) {

        String suffix = FileUtil.getSuffix(originalFilename);
        FileTypeEnum fileTypeEnum = null;
        if (StringUtils.isNotBlank(suffix)) {
            try {
                fileTypeEnum = FileTypeEnum.convert(suffix);
            } catch (Exception e) {
                LogUtil.warn(log, "获取文件的类型出现错误：{}", e.getMessage());
            }
        }

        if (StringUtils.isNotBlank(customerFilename)) {
            return new Filename(customerFilename + "." + suffix, suffix, fileTypeEnum);
        }

        return new Filename(originalFilename, suffix, fileTypeEnum);
    }

    /**
     * 根据目录层级创建File对象
     * @param names
     * @return
     */
    public static File getFile(@NotEmpty List<String> names) {
        StringBuilder sb = new StringBuilder();
        names.stream().forEach(name->{
            sb.append(name).append(File.separator);
        });

        // 去掉最后一个分隔符
        return new File(sb.substring(0, sb.length() - 1));
    }

    /**
     * 获取 临时目录/md5 这个文件对象
     * @param rootDir 根目录
     * @param fileMd5 文件的md5
     * @return
     */
    public static File getTempAndMd5File(@NotBlank String rootDir, String fileMd5){
        // 拼接文件存储的临时文件夹
        String fileTempDir = rootDir + File.separator + FileConst.TEMP_DIR + File.separator + fileMd5;
        // 文件夹不存在
        File file = new File(fileTempDir);

        // 存在
        if (file.exists()) {
            // 是普通文件
            if (file.isFile()) {
                // 将其改一个后缀
                File newFile = new File(file.getParent() + File.separator + file.getName() + ".bak");
                if (file.renameTo(newFile)) {
                    LogUtil.info(log, "文件名重命名成功，原文件名：{}，新文件名：{}", file.getName(), newFile.getName());
                }
                // 创建文件夹
                file = new File(fileTempDir);
                if (file.mkdirs()) {
                    LogUtil.info(log, "创建临时文件夹：{}成功", fileTempDir);
                }
            }

            // 是文件夹
            return file;
        }
        // 不存在，创建文件
        if (file.mkdirs()) {
            LogUtil.info(log, "创建临时文件夹：{}成功", fileTempDir);
        }

        return file;
    }

    /**
     * 创建file_po的link属性值
     * @param filePO
     * @return
     */
    public static String createFileLink(FilePO filePO) throws UnsupportedEncodingException {
        Long id = filePO.getId();
        String encode = URLEncoder.encode(Base64.getEncoder().encodeToString(id.toString().getBytes(StandardCharsets.UTF_8)), "UTF-8");
        return "http://localhost:api/file/file-link/info/"+encode;
    }
}
