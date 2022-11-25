package com.goudong.file.core;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.core.util.ArrayUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.file.exception.FileUploadException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 上传文件的配置
 * @author msi
 * @version 1.0
 * @date 2021/12/4 21:07
 */
@Data
@Slf4j
public class FileUpload {

    /**
     * 是否开启文件上传接口
     */
    private Boolean enabled = true;

    /**
     * 开启全路径模式
     */
    private Boolean enableFullPathModel = false;

    /**
     * 上传文件的目录,不论操作系统都使用'/'作为目录分隔符
     * 默认值是 用户目录下的'/goudong-file-server',使用日期作为二级目录.
     */
    private String rootDir = "";

    //~常见图片
    //==================================================================================================================
    @NestedConfigurationProperty
    private FileType jpg = new FileType(FileTypeEnum.JPG);
    @NestedConfigurationProperty
    private FileType jpeg = new FileType(FileTypeEnum.JPEG);
    @NestedConfigurationProperty
    private FileType png = new FileType(FileTypeEnum.PNG);
    @NestedConfigurationProperty
    private FileType gif = new FileType(FileTypeEnum.GIF);
    @NestedConfigurationProperty
    private FileType svg = new FileType(FileTypeEnum.SVG);

    //~常见office文件
    //==================================================================================================================
    @NestedConfigurationProperty
    private FileType xls = new FileType(FileTypeEnum.XLS);
    @NestedConfigurationProperty
    private FileType xlsx = new FileType(FileTypeEnum.XLSX);
    @NestedConfigurationProperty
    private FileType doc = new FileType(FileTypeEnum.DOC);
    @NestedConfigurationProperty
    private FileType docx = new FileType(FileTypeEnum.DOCX);
    @NestedConfigurationProperty
    private FileType ppt = new FileType(FileTypeEnum.PPT);
    @NestedConfigurationProperty
    private FileType pptx = new FileType(FileTypeEnum.PPTX);
    @NestedConfigurationProperty
    private FileType pdf = new FileType(FileTypeEnum.PDF);

    //~常见文本文档
    //==================================================================================================================
    @NestedConfigurationProperty
    private FileType txt = new FileType(FileTypeEnum.TXT);
    @NestedConfigurationProperty
    private FileType md = new FileType(FileTypeEnum.MD);

    //~常见音频
    //==================================================================================================================
    @NestedConfigurationProperty
    private FileType mp3 = new FileType(FileTypeEnum.MP3);

    //~常见视频
    //==================================================================================================================
    @NestedConfigurationProperty
    private FileType avi = new FileType(FileTypeEnum.AVI);
    @NestedConfigurationProperty
    private FileType mov = new FileType(FileTypeEnum.MOV);
    @NestedConfigurationProperty
    private FileType rmvb = new FileType(FileTypeEnum.RMVB);
    @NestedConfigurationProperty
    private FileType rm = new FileType(FileTypeEnum.RM);
    @NestedConfigurationProperty
    private FileType flv = new FileType(FileTypeEnum.FLV);
    @NestedConfigurationProperty
    private FileType mp4 = new FileType(FileTypeEnum.MP4);

    /**
     * 文件类型, 程序将用户配置和默认配置统一设置到属性中，方便后期使用。
     */
    private List<FileType> fileTypes = new ArrayList<>();

    //~ 上传相关校验
    //==================================================================================================================

    /**
     * 以下情况会失败：
     * <pre>
     *     1.{@code enabled = false}：关闭了上传文件配置
     *     2.{@code fileSuffix}类型文件，未配置允许上传。
     *     3.{@code fileSuffix}类型文件已配置允许上传，但是它的大小（{@code fileSize}）超过所配置允许的大小。
     * </pre>
     * @param fileSuffix 上传文件的原始后缀
     * @param fileSize 上传文件的大小
     */
    public void check(String fileSuffix, long fileSize) {
        FileType fileType = this
                .checkEnabled()
                .checkFileType(fileSuffix);
        fileType.checkFileSize(fileSize);
    }

    /**
     * 以下情况会失败：
     * <pre>
     *     1.{@code enabled = false}：关闭了上传文件配置
     *     2.{@code file}类型文件，未配置允许上传。
     *     3.{@code file}类型文件已配置允许上传，但是它的大小超过所配置允许的大小。
     * </pre>
     * @param file web上传的文件对象
     */
    public void check(MultipartFile file) {
        FileType fileType = this
                .checkEnabled()
                .checkFileType(file);
        fileType.checkFileSize(file);
    }

    /**
     * 校验是否开启上传配置
     * @return
     */
    public FileUpload checkEnabled() {
        // 检查是否激活文件上传功能
        AssertUtil.isTrue(this.enabled, () -> {
            // TODO 修改成活得
            String applicationName = "文件服务";
            String clientMessage = String.format("文件服务 %s 未开启上传文件", applicationName);
            String serverMessage = String.format("请设置属性 file.upload.enabled=true 即可解决问题");
            LogUtil.warn(log, "{}---{}", clientMessage, serverMessage);
            return new FileUploadException(ClientExceptionEnum.NOT_FOUND, "服务禁止上传，请稍后再试", "文件服务关闭了上传功能");
        });
        return this;
    }

    /**
     * 获取上传配置对应的FileType对象
     * @param file web上传的文件
     * @return
     */
    @SuppressWarnings("all")
    public FileType getFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int pos = originalFilename.lastIndexOf(".");
        // 没有后缀的文件，暂不允许上传
        AssertUtil.isNotEquals(-1, pos, () -> new FileUploadException(ClientExceptionEnum.BAD_REQUEST, "没有后缀的文件，暂不允许上传"));
        // 存在后缀，就比较后缀是否通过
        String fileSuffix = originalFilename.substring(pos + 1);
        // 存在后缀，就比较后缀是否通过
        return getFileType(fileSuffix);
    }

    /**
     * 获取上传配置对应的FileType对象
     * @param fileSuffix 文件后缀
     * @return
     */
    @SuppressWarnings("all")
    public FileType getFileType(String fileSuffix) {
        AssertUtil.isNotBlank(fileSuffix);
        return this.fileTypes // 用户配置的文件上传信息
                .stream()
                .filter(f -> f.getType().name().equalsIgnoreCase(fileSuffix))
                .findFirst()
                .orElseThrow(() -> BasicException.client("文件服务，暂不支持上传{}类型文件", ArrayUtil.create(fileSuffix)));
    }

    /**
     * 检查上传的文件对象{@code file}是否是配置上传的{@code FileType}
     * 因为{@code checkFileType} 和 {@code checkFileSize}有顺序和ThreadLocal问题，所以将其设置私有
     * @param file
     * @return
     */
    @SuppressWarnings("all")
    private FileType checkFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int pos = originalFilename.lastIndexOf(".");
        // 没有后缀的文件，暂不允许上传
        AssertUtil.isNotEquals(-1, pos, () -> new FileUploadException(ClientExceptionEnum.BAD_REQUEST, "没有后缀的文件，暂不允许上传"));
        // 存在后缀，就比较后缀是否通过
        String fileSuffix = originalFilename.substring(pos + 1);
        // 存在后缀，就比较后缀是否通过
        return checkFileType(fileSuffix);
    }

    /**
     * 根据文件后缀{@code fileSuffix}判断是否是配置上传的{@code FileType}
     * 因为{@code checkFileType} 和 {@code checkFileSize}有顺序和ThreadLocal问题，所以将其设置私有
     * @param fileSuffix 文件后缀
     * @return
     */
    @SuppressWarnings("all")
    private FileType checkFileType(String fileSuffix) {
        return this.getFileType(fileSuffix);
    }
}
