package com.goudong.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    /**
     * 请求
     */
    private final HttpServletRequest request;

    /**
     * 响应
     */
    private final HttpServletResponse response;

    public UploadServiceImpl(FileUpload fileUpload,
                             FileRepository fileRepository,
                             RedisTool redisTool,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        this.fileUpload = fileUpload;
        this.fileRepository = fileRepository;
        this.redisTool = redisTool;
        this.request = request;
        this.response = response;
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

        if (mergeShardUploadTemp(shardUploadDTO)) {
            LogUtil.info(log, "合并成功");
            return;
        }

        RedisKeyProviderEnum fileShardUploadProcessing = RedisKeyProviderEnum.FILE_SHARD_UPLOAD_PROCESSING;
        // 获取上传队列
        List<FileShardUploadStatusRedisDTO> statusRedisDTOS = redisTool.getList(
                fileShardUploadProcessing,
                FileShardUploadStatusRedisDTO.class,
                shardUploadDTO.getFileMd5()
        );
        // 该源文件已经开始处理了
        if (CollectionUtils.isNotEmpty(statusRedisDTOS)) {
            // 判断当前分片是否已经执行过
            Optional<FileShardUploadStatusRedisDTO> redisDTO = statusRedisDTOS.stream()
                    .filter(f -> Objects.equals(f.getShardIndex(), shardUploadDTO.getShardIndex()))
                    .findFirst();

            String key = redisTool.getKey(fileShardUploadProcessing, shardUploadDTO.getFileMd5());
            // 存在值就取出来
            if (redisDTO.isPresent()) {
                FileShardUploadStatusRedisDTO uploadStatusRedisDTO = redisDTO.get();
                // 该分片已经上传成功
                if (uploadStatusRedisDTO.getSucceed()) {
                    // TODO 成功了，告诉前端
                    return;
                }
                // 之前上传失败，本次分片重试上传
                // 上传
                int index = statusRedisDTOS.indexOf(uploadStatusRedisDTO);

                // 修改key指定索引的值，有则覆盖，没有则新增。
                redisTool.opsForList().set(key, index, saveShard2Temp(shardUploadDTO));
                // 刷新失效时长
                redisTool.refresh(fileShardUploadProcessing, shardUploadDTO.getFileMd5());
                return;
            }
            // 分片还未开始上传
            // 上传,设置到redis中,刷新过期时长
            redisTool.opsForList().leftPush(key, saveShard2Temp(shardUploadDTO));
            redisTool.refresh(fileShardUploadProcessing, shardUploadDTO.getFileMd5());
            // 返回成功
            return;
        }

        // 首先，查询数据库是否有值
        FilePO firstByFileMd5 = fileRepository.findFirstByFileMd5(shardUploadDTO.getFileMd5());
        if (firstByFileMd5 != null) {
            // 已经上传成功
            FilePO newFilePO = BeanUtil.copyProperties(firstByFileMd5, FilePO.class, "id");

            fileRepository.save(newFilePO);
            LogUtil.info(log, "秒传文件成功");
            return;
        }

        /*
            数据库不存在文件，开始分片上传
         */
        statusRedisDTOS.add(saveShard2Temp(shardUploadDTO));

        // 设置到redis中
        redisTool.set(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_PROCESSING, statusRedisDTOS, shardUploadDTO.getFileMd5());
    }

    /**
     * 将分片保存到临时文件
     */
    private FileShardUploadStatusRedisDTO saveShard2Temp(FileShardUploadDTO shardUploadDTO) {
        // 创建临时文件夹
        File temp = FileUtils.getTempAndMd5File(fileUpload.getRootDir(), shardUploadDTO.getFileMd5());
        // 创建临时文件
        File shardFile = new File(temp.getPath() + File.separator + shardUploadDTO.getShardIndex());

        FileShardUploadStatusRedisDTO build = FileShardUploadStatusRedisDTO.builder()
                .filePath(shardFile.getAbsolutePath())
                .shardIndex(shardUploadDTO.getShardIndex())
                .succeed(true)
                .build();
        try {
            shardUploadDTO.getShardData().transferTo(shardFile);
        } catch (IOException e) {
            build.setSucceed(false);
            e.printStackTrace();
        }

        return build;
    }

    /**
     * 合并分片上传的临时文件
     * @param shardUploadDTO 上传对象
     * @return true：合并完成。false：还不能合并
     */
    public boolean mergeShardUploadTemp(FileShardUploadDTO shardUploadDTO){
        // 获取上传队列
        List<FileShardUploadStatusRedisDTO> statusRedisDTOS = redisTool.getList(
                RedisKeyProviderEnum.FILE_SHARD_UPLOAD_PROCESSING,
                FileShardUploadStatusRedisDTO.class,
                shardUploadDTO.getFileMd5()
        );
        // 文件分片上传未完成，直接return。
        boolean boo = statusRedisDTOS.size() != shardUploadDTO.getShardTotal()
                && statusRedisDTOS.stream()
                .filter(FileShardUploadStatusRedisDTO::getSucceed)
                .count() != shardUploadDTO.getShardTotal();
        if (boo) {
            return false;
        }

        // 创建文件名
        String uuid = IdUtil.simpleUUID();
        String filename = uuid+"."+shardUploadDTO.getFileType();
        // 合并文件
        File file = FileUtils.getFile(Lists.newArrayList(fileUpload.getRootDir(), FileUtils.getDateDir(), filename));

        if (!file.exists()) {
            try {
                File parentFile = file.getParentFile();
                if (!file.getParentFile().exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileChannel fosChannel = null;
        try {
            fosChannel = new FileOutputStream(file, true).getChannel();
            // 将其写入
            String filePath = statusRedisDTOS.get(0).getFilePath();
            // 获取临时目录
            File parentFile = new File(filePath).getParentFile();
            File[] tempFiles = parentFile.listFiles();
            FileChannel finalFosChannel = fosChannel;
            Stream.of(tempFiles)
                    // 文件名就是分片的索引名，直接升序排序
                    .sorted((f1, f2)-> Long.valueOf(f1.getName()).compareTo(Long.valueOf(f2.getName())))
                    .forEach(p->{
                        // 先读文件
                        try {
                            // 使用nio
                            FileChannel fisChannel = new FileInputStream(p).getChannel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                            while(fisChannel.read(byteBuffer) != -1){
                                byteBuffer.flip();
                                finalFosChannel.write(byteBuffer);
                                byteBuffer.clear();
                            }

                            fisChannel.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fosChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 删除redis中的数据
        // redisTool.deleteKey(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_PROCESSING, shardUploadDTO.getFileMd5());

        return true;
    }
}
