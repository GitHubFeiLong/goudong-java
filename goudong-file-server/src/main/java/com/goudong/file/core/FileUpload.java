package com.goudong.file.core;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import lombok.Data;
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
     * 上传文件的目录,不论操作系统都使用'/'作为目录分隔符 默认值是 '/goudong-file-server'.
     */
    private String rootDir = "/goudong-file-server";

    /**
     * 文件类型
     */
    private List<FileType> fileTypes = new ArrayList<>();


    public void a (MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);



        fileTypes.forEach(p->{
            long size = file.getSize();
            long maxSize = p.getFileLengthUnit().toBytes(p.getLength());
            if (size > maxSize) {
                throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "文件超过配置的大小");
            }

        });
    }
}
