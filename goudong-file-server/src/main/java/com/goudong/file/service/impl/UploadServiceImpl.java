package com.goudong.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.goudong.commons.dto.file.FileDTO;
import com.goudong.commons.dto.file.FileShardUploadDTO;
import com.goudong.commons.dto.file.FileShardUploadStatusRedisDTO;
import com.goudong.commons.dto.file.RequestUploadDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.exception.file.FileUploadException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.core.Filename;
import com.goudong.file.enumerate.RedisKeyProviderEnum;
import com.goudong.file.po.FilePO;
import com.goudong.file.repository.FileRepository;
import com.goudong.file.service.UploadService;
import com.goudong.file.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 类描述：
 * 上传服务层接口实现类
 * @author msi
 * @version 1.0
 * @date 2021/12/11 23:25
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 自定义的文件上传的配置属性
     */
    private final FileUpload fileUpload;

    /**
     * 文件持久层
     */
    private final FileRepository fileRepository;

    /**
     * redis工具类
     */
    private final RedisTool redisTool;

    public UploadServiceImpl(FileUpload fileUpload, FileRepository fileRepository, RedisTool redisTool) {
        this.fileUpload = fileUpload;
        this.fileRepository = fileRepository;
        this.redisTool = redisTool;
    }

    /**
     * 检查文件是否允许上传
     * @param files
     */
    @Override
    public void checkSimpleUpload(List<MultipartFile> files) {
        // 检查是否激活文件上传功能
        if (!fileUpload.getEnabled()) {
            String clientMessage = String.format("文件服务 %s 未开启上传文件", this.applicationName);
            String serverMessage = String.format("请设置属性 file.upload.enabled=true 即可解决问题");
            // throw new FileUploadException(ServerExceptionEnum.SERVICE_UNAVAILABLE, clientMessage, serverMessage);
            LogUtil.warn(log, "{}---{}", clientMessage, serverMessage);
            return;
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
                        filter(f -> Objects.equals(suffix.toUpperCase(), f.getType().name()))
                        .filter(f-> f.getEnabled())
                        .findFirst();

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

                    throw new FileUploadException(ClientExceptionEnum.BAD_REQUEST, message);
                }
            } else {
                // 没有后缀的文件，暂不允许上传
                throw new FileUploadException(ClientExceptionEnum.BAD_REQUEST, "没有后缀的文件，暂不允许上传");
            }
        }
    }

    /**
     * 检查分段上传参数是否正确
     * @param shardUploadDTO
     */
    @Override
    public void checkShardUpload(FileShardUploadDTO shardUploadDTO) {
        FileTypeEnum fileTypeEnum = FileTypeEnum.convert(shardUploadDTO.getFileType());

        Optional<FileType> first = fileUpload.getFileTypes().stream().filter(file -> Objects.equals(file.getType(), fileTypeEnum)).findFirst();
        // 不存在指定类型的配置
        if (!first.isPresent()) {
            throw new FileUploadException(ClientExceptionEnum.BAD_REQUEST, "不支持上传当前文件类型");
        }

        FileType fileType = first.get();
        // 获取字节大小
        long bites = FileLengthUnit.BIT.convert(fileType.getLength(), fileType.getFileLengthUnit());
        if (bites < shardUploadDTO.getFileSize()) {
            throw new FileUploadException(ClientExceptionEnum.BAD_REQUEST,
                    "文件大小不符合配置",
                    String.format("该类型文件允许上传的最大文件大小为：%s字节", bites)
            );
        }
    }

    /**
     * 简单上传
     * @param requestUploadDTO
     * @return fileDto 上传成功的文件信息
     */
    @Override
    public FileDTO simpleUpload(RequestUploadDTO requestUploadDTO) {
        MultipartFile file = requestUploadDTO.getFile();
        String originalFilename = file.getOriginalFilename();
        Filename filename = FileUtils.getFilename(originalFilename);
        String customerFilename = requestUploadDTO.getOriginalFilename();
        if (StringUtils.isNotBlank(customerFilename) && !Objects.equals(customerFilename, "null")) {
            filename.setFilename(customerFilename);
        }

        // 文件保存的 目录
        String dir = fileUpload.getRootDir() + File.separator + LocalDateTime.now().toLocalDate().toString();
        File dirFile = new File(dir);
        // 不存在文件夹，再创建文件夹
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            LogUtil.warn(log, "创建文件夹失败");
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "文件已存在");
        }

        FilePO filePO = new FilePO();
        // 当前文件名，使用规则生成
        filePO.setCurrentFilename(IdUtil.simpleUUID());
        // 原文件名
        filePO.setOriginalFilename(filename.getFilename());
        // 文件类型
        filePO.setFileType(filename.getFileTypeEnum().lowerName());
        // 文件大小
        filePO.setSize(file.getSize());

        // 设置合适的大小和单位
        ImmutablePair<Long, FileLengthUnit> fileSizePair = FileUtils.adaptiveSize(file.getSize());
        // 文件长度
        filePO.setFileLength(fileSizePair.getLeft());
        // 长度单位
        filePO.setFileLengthUnit(fileSizePair.getRight().name().toLowerCase());

        // 判断文件是否存在
        String newFullFilename = dir + File.separator + filePO.getCurrentFilename()+ "." + filePO.getFileType();
        File newFile = new File(newFullFilename);
        if (newFile.exists()) {
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "文件已存在");
        }
        // 创建文件
        try(InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(newFile)) {
            StreamUtils.copy(in, out);
            filePO.setFileLink("");
            filePO.setFilePath(newFullFilename);

            fileRepository.save(filePO);
            return BeanUtil.copyProperties(filePO, FileDTO.class);
        } catch (IOException e) {
            LogUtil.error(log, "文件上传失败:{}", e);
            throw ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, "创建失败");
        }
    }

    /**
     * 分块上传
     *
     * @param shardUploadDTO
     */
    @Override
    public void shardUpload(FileShardUploadDTO shardUploadDTO) {
        // 1. 检查文件类型和大小是否符合标准
        MultipartFile shardData = shardUploadDTO.getShardData();

        // 获取上传队列
        List<FileShardUploadStatusRedisDTO> statusRedisDTOS = redisTool.getList(
                RedisKeyProviderEnum.FILE_SHARD_UPLOAD_PROCESSING,
                FileShardUploadStatusRedisDTO.class,
                shardUploadDTO.getFileMd5()
        );
        // 该源文件已经开始处理了
        if (CollectionUtils.isNotEmpty(statusRedisDTOS)) {
            // 判断当前分片是否已经执行过
            Optional<FileShardUploadStatusRedisDTO> redisDTO = statusRedisDTOS.stream()
                    .filter(f -> Objects.equals(f.getShardIndex(), shardUploadDTO.getShardIndex()))
                    .findFirst();
            // 存在值就取出来
            if (redisDTO.isPresent()) {
                FileShardUploadStatusRedisDTO fileShardUploadStatusRedisDTO = redisDTO.get();
                // 该分片已经上传成功
                if (fileShardUploadStatusRedisDTO.isSucceed()) {
                    // TODO 成功了，告诉前端
                    return;
                }
            }
            // 分片还未开始上传 or 分片上传失败 => 开始上传逻辑

        } else {
            // 首先，查询数据库是否有值
            FilePO firstByFileMd5 = fileRepository.findFirstByFileMd5(shardUploadDTO.getFileMd5());
            if (firstByFileMd5 != null) {
                // 已经上传成功
                FilePO newFilePO = BeanUtil.copyProperties(firstByFileMd5, FilePO.class, "id");

                fileRepository.save(newFilePO);
            }
            // 第一次上传



            statusRedisDTOS = new ArrayList<>();
            FileShardUploadStatusRedisDTO build = FileShardUploadStatusRedisDTO.builder().build();
            statusRedisDTOS.add(build);
        }




    }


}
