package com.goudong.file.controller.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.goudong.commons.dto.file.RequestUploadDTO;
import com.goudong.commons.dto.file.ResponseUploadDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.FileLengthUnit;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.pojo.Result;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：
 * 文件的基本操作：上传
 * @author msi
 * @date 2021/12/4 20:49
 * @version 1.0
 */
@Api(tags = "文件上传")
@Slf4j
@Validated
@RestController
@RequestMapping("/upload-group")
public class UploadController {

    @Resource
    private FileUpload fileUpload;

    @Resource
    private Environment environment;

    /**
     * 检查文件是否允许上传
     * @param files
     */
    public void check(List<MultipartFile> files) {
        // TODO 上传文件接口禁用
        // 检查是否激活文件上传功能
        if (!fileUpload.getEnabled()) {
            String applicationName = environment.getProperty("spring.application.name");
            String clientMessage = String.format("文件服务 %s 未开启上传文件", applicationName);
            String serverMessage = String.format("请设置属性 file.upload.enabled=true 即可解决问题");
            throw ServerException.serverException(ServerExceptionEnum.SERVICE_UNAVAILABLE, clientMessage, serverMessage);
        }

        // 类型及大小判断
        Iterator<MultipartFile> iterator = files.iterator();
        while (iterator.hasNext()) {
            MultipartFile file = iterator.next();

            // 用户配置的文件上传信息
            List<FileType> fileTypes = fileUpload.getFileTypes();

            // 比较类型及大小是否符合配置
            String originalFilename = file.getOriginalFilename();
            int pos = originalFilename.lastIndexOf(".");
            if (pos != -1) {
                // 存在后缀，就比较后缀是否通过
                String suffix = originalFilename.substring(pos + 1);
                Optional<FileType> first = fileTypes.stream().
                        filter(f -> Objects.equals(suffix.toUpperCase(), f.getType().name())).findFirst();
                FileType fileType = first.
                        orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.BAD_REQUEST,
                                String.format("文件服务，暂不支持上传%s类型文件", suffix)));

                // 计算用户配置该类型文件允许上传的字节大小
                long bytes = fileType.getFileLengthUnit().toBytes(fileType.getLength());
                if (bytes < file.getSize()) {
                    ImmutablePair<Long, FileLengthUnit> pair = FileUtils.adaptiveSize(file.getSize());
                    String message = String.format("%s文件(%s%s)超过配置(%s%s)",
                            originalFilename, pair.getLeft(), pair.getRight(),
                            fileType.getLength(), fileType.getFileLengthUnit());

                    throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, message);
                }
            } else {
                // 没有后缀的文件，暂不允许上传
                throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "没有后缀的文件，暂不允许上传");
            }

        }

    }
    // private final String fileDir = ConfigConstants.getFileDir();
    // private final String demoDir = "demo";
    // private final String demoPath = demoDir + File.separator;

    @ApiOperation("上传单文件")
    @PostMapping("/upload")
    public Result<ResponseUploadDTO> upload(@NotNull RequestUploadDTO requestUploadDTO) throws JsonProcessingException {
        MultipartFile file = requestUploadDTO.getFile();

        // 检查
        check(Lists.newArrayList(file));

        String originalFilename = requestUploadDTO.getOriginalFilename();

        int pos = originalFilename.lastIndexOf(".");

        if (pos == -1) {
            String fileType = originalFilename.substring(pos + 1);
        }


        // 获取文件名
        // String originalFilename = Optional.ofNullable().orElse(file.getOriginalFilename());

        // escaping dangerous characters to prevent XSS
        ///fileName = HtmlUtils.htmlEscape(fileName, StandardCharsets.UTF_8.name());

        ResponseUploadDTO responseUploadDTO = new ResponseUploadDTO();
        responseUploadDTO.setOriginalFilename(originalFilename);
        long size = file.getSize();
        responseUploadDTO.setSize(size);

        // 设置合适的大小和单位
        ImmutablePair<Long, FileLengthUnit> fileSizePair = FileUtils.adaptiveSize(size);
        responseUploadDTO.setFileLength(fileSizePair.getLeft());
        responseUploadDTO.setFileLengthUnit(fileSizePair.getRight());

        String dir = FileUtils.getDir(fileUpload.getRootDir());



        // // Check for Unix-style path
        // int unixSep = fileName.lastIndexOf('/');
        // // Check for Windows-style path
        // int winSep = fileName.lastIndexOf('\\');
        // // Cut off at latest possible point
        // int pos = (Math.max(winSep, unixSep));
        // if (pos != -1)  {
        //     fileName = fileName.substring(pos + 1);
        // }
        // // 判断是否存在同名文件
        // if (existsFile(fileName)) {
        //     return new ObjectMapper().writeValueAsString(ReturnResponse.failure("存在同名文件，请先删除原有文件再次上传"));
        // }
        // File outFile = new File(fileDir + demoPath);
        // if (!outFile.exists() && !outFile.mkdirs()) {
        //     logger.error("创建文件夹【{}】失败，请检查目录权限！",fileDir + demoPath);
        // }
        // logger.info("上传文件：{}", fileDir + demoPath + fileName);
        // try(InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(fileDir + demoPath + fileName)) {
        //     StreamUtils.copy(in, out);
        //     return new ObjectMapper().writeValueAsString(ReturnResponse.success(null));
        // } catch (IOException e) {
        //     logger.error("文件上传失败", e);
        //     return new ObjectMapper().writeValueAsString(ReturnResponse.failure());
        // }
        return null;
    }

    // @RequestMapping(value = "deleteFile", method = RequestMethod.GET)
    // public String deleteFile(String fileName) throws JsonProcessingException {
    //     if (fileName.contains("/")) {
    //         fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
    //     }
    //     File file = new File(fileDir + demoPath + fileName);
    //     logger.info("删除文件：{}", file.getAbsolutePath());
    //     if (file.exists() && !file.delete()) {
    //        logger.error("删除文件【{}】失败，请检查目录权限！",file.getPath());
    //     }
    //     return new ObjectMapper().writeValueAsString(ReturnResponse.success());
    // }
    //
    // @RequestMapping(value = "listFiles", method = RequestMethod.GET)
    // public String getFiles() throws JsonProcessingException {
    //     List<Map<String, String>> list = new ArrayList<>();
    //     File file = new File(fileDir + demoPath);
    //     if (file.exists()) {
    //         Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(file1 -> {
    //             Map<String, String> fileName = new HashMap<>();
    //             fileName.put("fileName", demoDir + "/" + file1.getName());
    //             list.add(fileName);
    //         });
    //     }
    //     return new ObjectMapper().writeValueAsString(list);
    // }
    //
    // private boolean existsFile(String fileName) {
    //     File file = new File(fileDir + demoPath + fileName);
    //     return file.exists();
    // }
}
