package com.goudong.file.controller.upload;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.dto.file.FileDTO;
import com.goudong.commons.dto.file.FileShardUploadDTO;
import com.goudong.commons.dto.file.RequestUploadDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.exception.file.FileUploadException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.core.Filename;
import com.goudong.file.po.FilePO;
import com.goudong.file.repository.FileRepository;
import com.goudong.file.service.UploadService;
import com.goudong.file.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：
 * 文件的基本操作：上传
 * 请求头 Range ： https://www.cnblogs.com/1995hxt/p/5692050.html
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

    /**
     * 文件服务服务层接口
     */
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }



    /**
     * 分段上传
     * @param requestUploadDTO
     * @param range 请求头中的range
     * @return
     * @throws JsonProcessingException
     */
    @ApiOperation("上传单文件")
    @PostMapping("/upload")
    @Transactional
    @Whitelist
    public Result<FileDTO> upload(@NotNull RequestUploadDTO requestUploadDTO) throws JsonProcessingException {
        // 检查
        uploadService.checkSimpleUpload(Lists.newArrayList(requestUploadDTO.getFile()));
        // 上传
        return Result.ofSuccess(uploadService.simpleUpload(requestUploadDTO)).clientMessage("上传成功");
    }

    /**
     * 分段上传
     * @param shardUploadDTO 分片参数对象
     * @return
     * @throws JsonProcessingException
     */
    @ApiOperation("分片上传文件")
    @PostMapping("/shard-upload")
    @Transactional
    @Whitelist
    public Result<Object> shardUpload(@Validated FileShardUploadDTO shardUploadDTO) throws JsonProcessingException {
        uploadService.checkShardUpload(shardUploadDTO);
        uploadService.shardUpload(shardUploadDTO);
        return Result.ofSuccess();
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
