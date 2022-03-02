package com.goudong.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.goudong.commons.constant.core.CommonConst;
import com.goudong.commons.dto.file.*;
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
import com.goudong.file.po.FileShardTaskPO;
import com.goudong.file.repository.FileRepository;
import com.goudong.file.repository.FileShardTaskRepository;
import com.goudong.file.service.UploadService;
import com.goudong.file.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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

    /**
     * 文件分片持久层接口
     */
    private final FileShardTaskRepository fileShardTaskRepository;

    public UploadServiceImpl(FileUpload fileUpload,
                             FileRepository fileRepository,
                             RedisTool redisTool,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             FileShardTaskRepository fileShardTaskRepository) {
        this.fileUpload = fileUpload;
        this.fileRepository = fileRepository;
        this.redisTool = redisTool;
        this.request = request;
        this.response = response;
        this.fileShardTaskRepository = fileShardTaskRepository;
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
    @Transactional
    @Override
    public FileShardUploadResultDTO shardUpload(FileShardUploadDTO shardUploadDTO) {
        // 查询分片上传任务
        List<FileShardTaskPO> taskPOS = fileShardTaskRepository.findAllByFileMd5(shardUploadDTO.getFileMd5());
        // 任务为空
        if (CollectionUtils.isEmpty(taskPOS)) {
            // 查询文件是否符合秒传
            FilePO firstByFileMd5 = fileRepository.findFirstByFileMd5(shardUploadDTO.getFileMd5());
            // 文件不为空，秒传
            if (firstByFileMd5 != null) {
                // 秒传
                return FileShardUploadResultDTO.createEntiretySuccessful();
            }

            // 文件不存在，创建任务
            taskPOS = initFileShardTasks(shardUploadDTO);

            // 保存到数据库
            fileShardTaskRepository.saveAll(taskPOS);
        } else {
            // 不是第一次，需要判断最后修改时间是否相同
            Date lastModifiedTime = taskPOS.get(0).getLastModifiedTime();

            // 时间不相等
            if (!Objects.equals(lastModifiedTime.getTime(), shardUploadDTO.getLastModifiedTime().getTime())) {
                // 清除之前创建的任务，删除之前保存的临时文件，从新创建任务
                taskPOS.stream().forEach(task->task.setDeleted(true));
                String tempPath = taskPOS.get(0).getTempPath();
                // hutool递归删除
                FileUtil.del(new File(tempPath).getParentFile());
                // 创建新的任务
                taskPOS = initFileShardTasks(shardUploadDTO);
                // 保存到数据库
                fileShardTaskRepository.saveAll(taskPOS);
            }
        }

        // 获取本次的任务对象。
        FileShardTaskPO fileShardTaskPO = taskPOS.stream().filter(f -> f.getShardIndex() == shardUploadDTO.getShardIndex()).findFirst().get();

        // 本次分片之前已经上传成功过了
        if (fileShardTaskPO.getSuccessful()) {
            return FileShardUploadResultDTO.createShardSuccessful(0, new long[]{}, new long[]{});
        }

        // 上传本次分片
        saveShard2Temp(shardUploadDTO, fileShardTaskPO);

        // 保存任务的状态等信息
        fileShardTaskRepository.save(fileShardTaskPO);

        // 判断是否需要合并文件了
        long successfulCount = taskPOS.stream().filter(FileShardTaskPO::getSuccessful).count();
        int taskTotal = taskPOS.size();
        if (successfulCount == taskTotal) {
            // 合并文件
            mergeShardUploadTemp(shardUploadDTO, taskPOS);
            LogUtil.info(log, "合并成功");
            return FileShardUploadResultDTO.createEntiretySuccessful();
        }

        // 本次上传成功
        List<Long> successfulShardIndexList = taskPOS.stream()
                .filter(FileShardTaskPO::getSuccessful)
                .map(FileShardTaskPO::getShardIndex)
                .collect(Collectors.toList());

        List<Long> unsuccessfulShardIndexList = taskPOS.stream()
                .filter(f->!f.getSuccessful())
                .map(FileShardTaskPO::getShardIndex)
                .collect(Collectors.toList());

        return FileShardUploadResultDTO.createShardSuccessful((int)Math.floor(successfulCount / taskTotal),
                ArrayUtils.toPrimitive(successfulShardIndexList.toArray(new Long[successfulShardIndexList.size()])),
                ArrayUtils.toPrimitive(unsuccessfulShardIndexList.toArray(new Long[unsuccessfulShardIndexList.size()]))
                );

    }

    /**
     * 初始化创建分片任务列表
     * @param shardUploadDTO
     * @return taskPOS 分片任务集合
     */
    private List<FileShardTaskPO> initFileShardTasks(FileShardUploadDTO shardUploadDTO) {
        List<FileShardTaskPO> taskPOS = new ArrayList<>();
        // 第一次上传
        for (long i = 0, total = shardUploadDTO.getShardTotal(); i < total; i++) {
            taskPOS.add(
                    FileShardTaskPO.builder()
                            .fileMd5(shardUploadDTO.getFileMd5())
                            .blockSize(shardUploadDTO.getBlockSize())
                            .successful(false)
                            .tempPath("")
                            .shardIndex(i)
                            .lastModifiedTime(shardUploadDTO.getLastModifiedTime())
                            .build()
            );
        }
        return taskPOS;
    }


    /**
     * 将分片保存到临时文件
     */
    private void saveShard2Temp(FileShardUploadDTO shardUploadDTO, FileShardTaskPO fileShardTaskPO) {
        // 创建临时文件夹
        File temp = FileUtils.getTempAndMd5File(fileUpload.getRootDir(), shardUploadDTO.getFileMd5());
        // 创建临时文件
        File shardFile = new File(temp.getPath() + File.separator + shardUploadDTO.getShardIndex());

        fileShardTaskPO.setTempPath(shardFile.getAbsolutePath());
        try {
            shardUploadDTO.getShardData().transferTo(shardFile);
            fileShardTaskPO.setSuccessful(true);
        } catch (IOException e) {
            fileShardTaskPO.setSuccessful(false);
            LogUtil.error(log, "上传分片失败：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 合并分片上传的临时文件
     * @param taskPOS 上传任务对象集合
     * @return true：合并完成。false：还不能合并
     */
    public boolean mergeShardUploadTemp(FileShardUploadDTO shardUploadDTO, List<FileShardTaskPO> taskPOS){
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
            // 获取临时目录
            File parentFile = new File(taskPOS.get(0).getTempPath()).getParentFile();
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

        return true;
    }
}
