package com.goudong.file.controller.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.goudong.commons.dto.file.RequestUploadDTO;
import com.goudong.commons.dto.file.ResponseUploadDTO;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.pojo.Result;
import com.goudong.file.core.FileUpload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
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
     * 检查是否激活文件上传功能
     */
    private void checkUploadEnabled() {
        // TODO 上传文件接口禁用
        if (!fileUpload.getEnabled()) {
            String applicationName = environment.getProperty("spring.application.name");
            String clientMessage = String.format("文件服务 %s 未开启上传文件", applicationName);
            String serverMessage = String.format("请设置属性 file.upload.enabled=true 即可解决问题");
            throw ServerException.serverException(ServerExceptionEnum.SERVICE_UNAVAILABLE, clientMessage, serverMessage);
        }
    }
    // private final String fileDir = ConfigConstants.getFileDir();
    // private final String demoDir = "demo";
    // private final String demoPath = demoDir + File.separator;
    //
    @ApiOperation("上传单文件")
    @PostMapping("/upload")
    public Result<ResponseUploadDTO> upload(@NotNull RequestUploadDTO requestUploadDTO) throws JsonProcessingException {
        checkUploadEnabled();

        MultipartFile file = requestUploadDTO.getFile();

        // 获取文件名
        String fileName = Optional.ofNullable(requestUploadDTO.getOriginalFilename()).orElse(file.getOriginalFilename());




        // escaping dangerous characters to prevent XSS
        ///fileName = HtmlUtils.htmlEscape(fileName, StandardCharsets.UTF_8.name());

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
