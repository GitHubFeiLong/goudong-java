package com.goudong.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.commons.constant.core.CommonConst;
import com.goudong.commons.enumerate.file.FileLengthUnit;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.core.FileUpload;
import com.goudong.file.core.Filename;
import com.goudong.file.dto.*;
import com.goudong.file.po.FilePO;
import com.goudong.file.po.FileShardTaskPO;
import com.goudong.file.repository.FileRepository;
import com.goudong.file.repository.FileShardTaskRepository;
import com.goudong.file.service.FileShardTaskService;
import com.goudong.file.service.UploadService;
import com.goudong.file.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
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

    /**
     * 文件分片服务层接口
     */
    private final FileShardTaskService fileShardTaskService;

    public UploadServiceImpl(FileUpload fileUpload,
                             FileRepository fileRepository,
                             RedisTool redisTool,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             FileShardTaskRepository fileShardTaskRepository,
                             FileShardTaskService fileShardTaskService) {
        this.fileUpload = fileUpload;
        this.fileRepository = fileRepository;
        this.redisTool = redisTool;
        this.request = request;
        this.response = response;
        this.fileShardTaskRepository = fileShardTaskRepository;
        this.fileShardTaskService = fileShardTaskService;
    }


    /**
     * 预检查文件类型合大小是否符合,并返回上传状态
     * 当文件上传完成过（存在md5相同的已完成上传记录），直接返回上传成功（秒传）
     * 当文件上传一部分，此时比较文件块大小，最后修改时间等有没有变化，如果没变化，就返回上传的二百分比以及成功片和失败片索引的数组
     * 如果变化了，就删除原分片任务，重新初始化分片任务。
     * 当文件未上传，初始化分片任务。
     * @param parameterDTO
     */
    @SuppressWarnings("all")
    @Override
    public ShardPrefixCheckReturnDTO shardPrefixCheck(ShardPrefixCheckParameterDTO parameterDTO) {
        // 检查是否激活文件上传功能、检查文件类型、检查文件大小
        fileUpload.check(parameterDTO.getFileType(), parameterDTO.getFileSize());

        // 判断文件是否存在
        FilePO firstByFileMd5 = fileRepository.findFirstByFileMd5(parameterDTO.getFileMd5());
        if (firstByFileMd5 != null) {
            return ShardPrefixCheckReturnDTO.builder().entiretySuccessful(true).percentage(100).build();
        }

        // 获取分片任务
        List<FileShardTaskPO> taskPOS = fileShardTaskService.listByFileMd5(parameterDTO.getFileMd5());

        if (CollectionUtils.isEmpty(taskPOS)) {
            // 文件不存在，创建任务
            // 创建新的任务
            FileShardUploadDTO shardUploadDTO = BeanUtil.copyProperties(parameterDTO, FileShardUploadDTO.class);
            taskPOS = fileShardTaskService.initFileShardTasks(shardUploadDTO);
        } else {
            // 不是第一次
            // 需要判断最后修改时间是否相同
            Date lastModifiedTime = taskPOS.get(0).getLastModifiedTime();
            boolean lastModifiedTimeUnequal = !Objects.equals(lastModifiedTime.getTime(), parameterDTO.getLastModifiedTime().getTime());
            if (lastModifiedTimeUnequal) {
                LogUtil.warn(log, "文件的最后修改时间不相同，需要重新初始化任务列表");
            }
            // 需要判断文件的分块是否相同
            boolean blockSizeUnequal = !Objects.equals(parameterDTO.getBlockSize(), taskPOS.get(0).getBlockSize());
            if (blockSizeUnequal) {
                LogUtil.warn(log, "文件分片上传的块大小有修改，需要重新初始化任务列表");
            }

            // 时间不相等
            if (lastModifiedTimeUnequal || blockSizeUnequal) {

                // 清除之前创建的任务，删除之前保存的临时文件，从新创建任务
                // 将任务进行删除(物理删除)
                fileShardTaskRepository.deleteInBatch(taskPOS);
                String tempPath = taskPOS.get(0).getTempPath();
                // hutool递归删除
                FileUtil.del(new File(tempPath).getParentFile());
                // 创建新的任务
                FileShardUploadDTO shardUploadDTO = BeanUtil.copyProperties(parameterDTO, FileShardUploadDTO.class);
                taskPOS = fileShardTaskService.initFileShardTasks(shardUploadDTO);
            }
        }

        List<Long> successfulList = taskPOS.stream().filter(FileShardTaskPO::getSuccessful).map(FileShardTaskPO::getShardIndex).collect(Collectors.toList());
        List<Long> unsuccessfulList = taskPOS.stream().filter(f->!f.getSuccessful()).map(FileShardTaskPO::getShardIndex).collect(Collectors.toList());

        int ss = successfulList.size();
        int uss = unsuccessfulList.size();
        // 使用double避免商值为0
        int percentage = (int)((ss * 1.0 / (ss + uss)) * 100);
        return ShardPrefixCheckReturnDTO.builder()
                .entiretySuccessful(false)
                .successfulShardIndexArray(successfulList)
                .unsuccessfulShardIndexArray(unsuccessfulList)
                .percentage(percentage)
                .blockSize(taskPOS.get(0).getBlockSize())
                .build();
    }

    /**
     * 检查文件是否允许上传
     * @param files
     */
    @Override
    public void checkSimpleUpload(List<MultipartFile> files) {
        // 类型及大小判断
        Iterator<MultipartFile> iterator = files.iterator();
        while (iterator.hasNext()) {
            MultipartFile file = iterator.next();
            fileUpload.check(file);// 检查文件类型,检查文件大小
        }
    }

    /**
     * 检查分段上传参数是否正确
     * @param shardUploadDTO
     */
    @Override
    public void checkShardUpload(FileShardUploadDTO shardUploadDTO) {
        // 类型及大小判断
        fileUpload.check(shardUploadDTO.getFileType(), shardUploadDTO.getFileSize());
    }

    /**
     * 检查文件导入是否符合配置的文件上传和导入的属性
     *
     * @param file
     */
    @Override
    public void checkImportUpload(MultipartFile file) {
        // 比较类型及大小是否符合配置
        fileUpload.check(file);
    }

    /**
     * 简单上传
     * @param requestUploadDTO
     * @return fileDto 上传成功的文件信息
     */
    @Override
    public FileDTO simpleUpload(RequestUploadDTO requestUploadDTO) throws IOException {
        MultipartFile file = requestUploadDTO.getFile();
        // 获取文件的md5值
        String md5Hex = DigestUtils.md5Hex(file.getInputStream());
        // 查询是否已有上传
        FilePO firstByFileMd5 = fileRepository.findFirstByFileMd5(md5Hex);
        String originalFilename = file.getOriginalFilename();
        String customerFilename = requestUploadDTO.getOriginalFilename();
        if (firstByFileMd5 != null) {
            LogUtil.info(log, "上传文件的md5值：{}，与数据库中id={}的md5相同，触发秒传。", md5Hex, firstByFileMd5.getId());
            FilePO filePO = BeanUtil.copyProperties(firstByFileMd5, FilePO.class, CommonConst.BASE_PO_FIELDS);
            originalFilename = StringUtils.isNotBlank(customerFilename)
                    ? new StringBuilder(customerFilename).append(".").append(firstByFileMd5.getFileType().toLowerCase()).toString()
                    : originalFilename;
            filePO.setOriginalFilename(originalFilename);

            fileRepository.save(filePO);
            return BeanUtil.copyProperties(filePO, FileDTO.class);
        }

        Filename filename = FileUtils.getFilename(customerFilename, originalFilename);

        // 文件保存的 目录
        String dir = fileUpload.getRootDir() + File.separator + LocalDateTime.now().toLocalDate().toString();
        File dirFile = new File(dir);
        // 不存在文件夹，再创建文件夹
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            LogUtil.warn(log, "创建文件夹失败");
            throw ClientException.client(ClientExceptionEnum.BAD_REQUEST, "文件已存在");
        }

        FilePO filePO = new FilePO();
        // 当前文件名，使用规则生成
        filePO.setCurrentFilename(IdUtil.simpleUUID() + "." + filename.getFileTypeEnum().lowerName());
        // 原文件名
        filePO.setOriginalFilename(filename.getFilename());
        // 文件类型
        filePO.setFileType(filename.getFileTypeEnum().name());
        // 文件大小
        filePO.setSize(file.getSize());

        // 设置合适的大小和单位
        ImmutablePair<Long, FileLengthUnit> fileSizePair = FileUtils.adaptiveSize(file.getSize());
        // 文件长度
        filePO.setFileLength(fileSizePair.getLeft());
        // 长度单位
        filePO.setFileLengthUnit(fileSizePair.getRight().name());

        // 判断文件是否存在
        String newFullFilename = dir + File.separator + filePO.getCurrentFilename();
        File newFile = new File(newFullFilename);
        if (newFile.exists()) {
            throw ClientException.client(ClientExceptionEnum.BAD_REQUEST, "文件已存在");
        }
        // 创建文件
        try(InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(newFile)) {
            StreamUtils.copy(in, out);
            filePO.setFilePath(newFullFilename);
            filePO.setFileMd5(DigestUtils.md5Hex(new FileInputStream(newFullFilename)));
            fileRepository.save(filePO);
            filePO.setFileLink(FileUtils.createFileLink(filePO));
            LogUtil.info(log, "文件上传成功");
            return BeanUtil.copyProperties(filePO, FileDTO.class);
        } catch (IOException e) {
            log.error("文件上传失败:{}", e);
            throw ServerException.server(ServerExceptionEnum.SERVER_ERROR, "创建失败", e.getMessage());
        }
    }

    /**
     * 分块上传
     *
     * @param shardUploadDTO
     */
    @Transactional
    @Override
    public FileShardUploadResultDTO shardUpload(FileShardUploadDTO shardUploadDTO) throws IOException {
        // 获取分片任务
        List<FileShardTaskPO> taskPOS = fileShardTaskService.listByFileMd5(shardUploadDTO.getFileMd5());
        // 任务为空
        if (CollectionUtils.isEmpty(taskPOS)) {
            // 查询文件是否符合秒传
            FilePO firstByFileMd5 = fileRepository.findFirstByFileMd5(shardUploadDTO.getFileMd5());
            // 文件不为空，秒传
            if (firstByFileMd5 != null) {
                LogUtil.info(log, "对比文件md5相同，执行秒传逻辑成功。file表id={}", firstByFileMd5.getId());
                // 秒传
                FileDTO fileDTO = BeanUtil.copyProperties(firstByFileMd5, FileDTO.class);
                return FileShardUploadResultDTO.createEntiretySuccessful(fileDTO);
            }

            // 文件不存在，创建任务
            taskPOS = fileShardTaskService.initFileShardTasks(shardUploadDTO);

        } else {
            // 不是第一次
            // 需要判断最后修改时间是否相同
            Date lastModifiedTime = taskPOS.get(0).getLastModifiedTime();
            boolean lastModifiedTimeUnequal = !Objects.equals(lastModifiedTime.getTime(), shardUploadDTO.getLastModifiedTime().getTime());
            if (lastModifiedTimeUnequal) {
                LogUtil.warn(log, "文件的最后修改时间不相同，需要重新初始化任务列表");
            }
            // 需要判断文件的分块是否相同
            boolean blockSizeUnequal = !Objects.equals(shardUploadDTO.getBlockSize(), taskPOS.get(0).getBlockSize());
            if (blockSizeUnequal) {
                LogUtil.warn(log, "文件分片上传的块大小有修改，需要重新初始化任务列表");
            }

            // 时间不相等，块也不相同
            if (lastModifiedTimeUnequal || blockSizeUnequal) {
                // 清除之前创建的任务，删除之前保存的临时文件，从新创建任务
                // 将任务进行删除(物理删除)
                fileShardTaskRepository.deleteInBatch(taskPOS);
                String tempPath = taskPOS.get(0).getTempPath();
                // hutool递归删除
                FileUtil.del(new File(tempPath).getParentFile());
                // 创建新的任务
                taskPOS = fileShardTaskService.initFileShardTasks(shardUploadDTO);
            }
        }

        // 获取本次的任务对象。
        FileShardTaskPO fileShardTaskPO = taskPOS.stream().filter(f -> f.getShardIndex() == shardUploadDTO.getShardIndex()).findFirst().get();

        // 本次分片之前已经上传成功过了
        if (fileShardTaskPO.getSuccessful()) {
            LogUtil.debug(log, "本次分片({})之前已经上传成功过了", shardUploadDTO.getShardIndex());
            List<Long> successfulList = taskPOS.stream().filter(FileShardTaskPO::getSuccessful).map(FileShardTaskPO::getShardIndex).collect(Collectors.toList());
            List<Long> unsuccessfulList = taskPOS.stream().filter(f->!f.getSuccessful()).map(FileShardTaskPO::getShardIndex).collect(Collectors.toList());
            ImmutablePair<Boolean, FilePO> immutablePair = checkMerge(shardUploadDTO, taskPOS);
            if (immutablePair.getLeft()){
                return FileShardUploadResultDTO.createEntiretySuccessful(
                        BeanUtil.copyProperties(immutablePair.getRight(), FileDTO.class)
                );
            }

            return FileShardUploadResultDTO.createShardSuccessful(successfulList, unsuccessfulList);
        }

        // 上传本次分片
        saveShard2Temp(shardUploadDTO, fileShardTaskPO, taskPOS);

        ImmutablePair<Boolean, FilePO> immutablePair = checkMerge(shardUploadDTO, taskPOS);
        if (immutablePair.getLeft()){
            return FileShardUploadResultDTO.createEntiretySuccessful(
                    BeanUtil.copyProperties(immutablePair.getRight(), FileDTO.class)
            );
        }

        // 本次上传成功
        List<Long> successfulList = taskPOS.stream().filter(FileShardTaskPO::getSuccessful).map(FileShardTaskPO::getShardIndex).collect(Collectors.toList());
        List<Long> unsuccessfulList = taskPOS.stream().filter(f->!f.getSuccessful()).map(FileShardTaskPO::getShardIndex).collect(Collectors.toList());

        return FileShardUploadResultDTO.createShardSuccessful(successfulList, unsuccessfulList);
    }

    /**
     * 将分片保存到临时文件
     */
    private void saveShard2Temp(FileShardUploadDTO shardUploadDTO, FileShardTaskPO fileShardTaskPO, List<FileShardTaskPO> taskPOS) throws IOException {
        // 创建临时文件夹
        File temp = FileUtils.getTempAndMd5File(fileUpload.getRootDir(), shardUploadDTO.getFileMd5());
        // 创建临时文件
        File shardFile = new File(temp.getPath() + File.separator + shardUploadDTO.getShardIndex());

        fileShardTaskPO.setTempPath(shardFile.getAbsolutePath());
        try {
            shardUploadDTO.getShardData().transferTo(shardFile);
            fileShardTaskPO.setSuccessful(true);
            LogUtil.debug(log, "上传分片({})成功", shardUploadDTO.getShardIndex());
        } catch (IOException e) {
            fileShardTaskPO.setSuccessful(false);
            log.error("上传分片({})失败：{}", shardUploadDTO.getShardIndex(), e.getMessage());
            // 原封不动跑出去
            throw e;
        } finally {
            // 保存任务的状态等信息
            fileShardTaskService.save(fileShardTaskPO, taskPOS);
        }
    }

    /**
     * 检查是否能进行合并，能合并时就进行合并
     * @param shardUploadDTO
     * @param taskPOS
     * @return immutablePair left(true:合并成功；false：合并失败),right 文件对象
     * @throws IOException
     */
    private ImmutablePair<Boolean, FilePO> checkMerge(FileShardUploadDTO shardUploadDTO, List<FileShardTaskPO> taskPOS) throws IOException {
        // 判断是否需要合并文件了
        long successfulCount = taskPOS.stream().filter(FileShardTaskPO::getSuccessful).count();
        int taskTotal = taskPOS.size();
        if (successfulCount == taskTotal) {
            fileShardTaskRepository.flush();
            LogUtil.info(log, "开始合并分片");
            // 合并文件
            FilePO filePO = mergeShardUploadTemp(shardUploadDTO, taskPOS);
            LogUtil.info(log, "合并成功");

            return ImmutablePair.of(true, filePO);
        }

        return ImmutablePair.of(false, null);
    }

    /**
     * 合并分片上传的临时文件
     * @param taskPOS 上传任务对象集合
     * @return true：合并完成。false：还不能合并
     */
    private FilePO mergeShardUploadTemp(FileShardUploadDTO shardUploadDTO, List<FileShardTaskPO> taskPOS) throws IOException {
        // 创建文件名
        String uuid = IdUtil.simpleUUID();
        String filename = uuid + "." + shardUploadDTO.getFileType().toLowerCase();
        // 合并文件
        File file = FileUtils.getFile(Lists.newArrayList(fileUpload.getRootDir(), LocalDateTime.now().toLocalDate().toString(), filename));

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

            // 保存文件
            ImmutablePair<Long, FileLengthUnit> longFileLengthUnitImmutablePair = FileUtils.adaptiveSize(file.length());
            FilePO filePO = new FilePO()
                    .setSize(file.length())
                    .setFileLength(longFileLengthUnitImmutablePair.getLeft())
                    .setFileLengthUnit(longFileLengthUnitImmutablePair.getRight().name())
                    .setCurrentFilename(file.getName())
                    .setOriginalFilename(shardUploadDTO.getFileName())
                    .setFilePath(file.getPath())
                    .setFileType(shardUploadDTO.getFileType())
                    .setFileMd5(shardUploadDTO.getFileMd5())
                    .setFileLink("link")
                    ;
            fileRepository.save(filePO);
            filePO.setFileLink(FileUtils.createFileLink(filePO));
            LogUtil.info(log, "文件合并并保存成功");
            fileShardTaskService.deleteAll(taskPOS);

            return filePO;
        } catch (IOException e) {
            LogUtil.error(log , "合并文件失败：{}", e.getMessage());
            throw e;
        } finally {
            fosChannel.close();
        }

    }
}
