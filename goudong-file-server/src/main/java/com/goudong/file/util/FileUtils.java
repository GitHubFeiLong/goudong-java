package com.goudong.file.util;

import cn.hutool.core.io.FileUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.commons.exception.file.FileUploadException;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.SpringBeanTool;
import com.goudong.file.constant.FileConst;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.core.Filename;
import com.goudong.file.po.FilePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.core.env.Environment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

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
        // String encode = URLEncoder.encode(Base64.getEncoder().encodeToString(id.toString().getBytes(StandardCharsets.UTF_8)), "UTF-8");
        Environment env = SpringBeanTool.getBean(Environment.class);
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
                .ofNullable(env.getProperty("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        // 拼接
        return new StringBuilder(protocol)
                .append("://")
                .append(hostAddress)
                .append(":")
                .append(serverPort)
                .append(contextPath)
                .append("/file-link/")
                .append(id).toString();
    }

    /**
     * 检查上传文件大小和类型是否满足配置条件
     * @param fileUpload
     * @param fileType
     * @param fileSize
     */
    public static void checkSatisfyUploadConfigurationConditions(FileUpload fileUpload, String fileType, Long fileSize) {
        // 检查是否激活文件上传功能
        if (!fileUpload.getEnabled()) {
            // TODO 修改成活得
            String applicationName = "文件服务";
            String clientMessage = String.format("文件服务 %s 未开启上传文件", applicationName);
            String serverMessage = String.format("请设置属性 file.upload.enabled=true 即可解决问题");
            LogUtil.warn(log, "{}---{}", clientMessage, serverMessage);
            throw new FileUploadException(ClientExceptionEnum.NOT_FOUND, "服务禁止上传，请稍后再试", "文件服务关闭了上传功能");
        }

        // 用户配置的文件上传信息
        List<FileType> fileTypes = fileUpload.getFileTypes();

        // 存在后缀，就比较后缀是否通过
        Optional<FileType> first = fileTypes.stream().
                filter(f -> f.getType().name().equalsIgnoreCase(fileType))
                .findFirst();

        if (!first.isPresent()) {
            throw new ClientException(ClientExceptionEnum.BAD_REQUEST, String.format("文件服务，暂不支持上传%s类型文件", fileType));
        }

        // 计算用户配置该类型文件允许上传的字节大小
        FileType fileTypeInstance = first.get();
        long bytes = fileTypeInstance.getFileLengthUnit().toBytes(fileTypeInstance.getLength());
        if (bytes < fileSize) {
            ImmutablePair<Long, FileLengthUnit> var1 = FileUtils.adaptiveSize(fileSize);
            String message = String.format("文件(类型:%s,size:%s%s)超过配置(%s%s)",
                    fileType,
                    var1.getLeft(),
                    var1.getRight(),
                    fileTypeInstance.getLength(),
                    fileTypeInstance.getFileLengthUnit());

            throw new FileUploadException(ClientExceptionEnum.BAD_REQUEST, message);
        }
    }

}
