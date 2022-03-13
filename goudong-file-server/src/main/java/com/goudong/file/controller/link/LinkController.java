package com.goudong.file.controller.link;

import com.goudong.commons.dto.file.FileDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.file.po.FilePO;
import com.goudong.file.repository.FileRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

/**
 * 类描述：
 * 文件的地址
 * @author msi
 * @version 1.0
 * @date 2022/3/13 9:23
 */
@Api(tags = "文件动态接口")
@RequestMapping("/file-link")
@RestController
public class LinkController {

    //~fields
    //==================================================================================================================
    private final FileRepository fileRepository;

    //~methods
    //==================================================================================================================


    public LinkController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * 获取文件的基本信息
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "获取文件信息")
    @GetMapping("/info/{encode}")
    public Result<FileDTO> fileLink(@PathVariable("encode") String encode) throws UnsupportedEncodingException {

        String decode = URLDecoder.decode(encode, "UTF-8");
        Long fileId = Long.valueOf(new String(Base64.getDecoder().decode(decode)));

        FilePO filePO = fileRepository.findById(fileId)
                .orElseThrow(()-> ClientException.clientException(ClientExceptionEnum.NOT_FOUND));
        FileDTO fileDTO = BeanUtil.copyProperties(filePO, FileDTO.class);

        return Result.ofSuccess(fileDTO);
    }



}