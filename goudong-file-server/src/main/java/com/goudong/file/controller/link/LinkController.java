package com.goudong.file.controller.link;

import cn.hutool.core.io.FileUtil;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.dto.file.FileDTO;
import com.goudong.commons.exception.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.po.FilePO;
import com.goudong.file.repository.FileRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * 类描述：
 * 文件的地址
 * @author msi
 * @version 1.0
 * @date 2022/3/13 9:23
 */
@Api(tags = "文件动态接口")
@RequestMapping("/file-link")
@Controller
@Slf4j
public class LinkController {

    //~fields
    //==================================================================================================================
    /**
     * request设置fileId的属性key
     */
    public static final String FILE_ID = "FILE_ID";

    /**
     * 文件持久层接口
     */
    private final FileRepository fileRepository;

    /**
     * 请求对象
     */
    private final HttpServletRequest request;

    /**
     * 响应对象
     */
    private final HttpServletResponse response;

    //~methods
    //==================================================================================================================


    public LinkController(FileRepository fileRepository, HttpServletRequest request, HttpServletResponse response) {
        this.fileRepository = fileRepository;
        this.request = request;
        this.response = response;
    }

    /**
     * 获取文件的基本信息
     * @param fileId 文件id
     * @param type 请求接口类型，0:获取文件基本信息；1：获取文件流；
     * @return
     * @throws UnsupportedEncodingException
     */
    @Whitelist
    @ApiOperation(value = "获取文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "路径参数，文件id", required = true),
            @ApiImplicitParam(name = "type", value = "类型(0:获取文件基本信息;1:获取文件)", example = "0", defaultValue = "1")
    })
    @GetMapping("/{fileId}")
    public String fileLink(@PathVariable("fileId") Long fileId, Integer type, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setAttribute(LinkController.FILE_ID, fileId);
        type = Optional.ofNullable(type).orElse(1);
        switch (type) {
            case 0:
                return "forward:/file-link/file-info";
            default:
                return "forward:/file-link/file-stream";
        }
    }


    @ApiIgnore
    @GetMapping("/file-info")
    @ResponseBody
    public Result<FileDTO> fileInfo() {
        long fileId = (long)request.getAttribute(LinkController.FILE_ID);
        FilePO filePO = fileRepository.findById(fileId)
                .orElseThrow(()-> ClientException.client(ClientExceptionEnum.NOT_FOUND));
        FileDTO fileDTO = BeanUtil.copyProperties(filePO, FileDTO.class);
        fileDTO.setMimeType(request.getServletContext().getMimeType(filePO.getCurrentFilename()));
        return Result.ofSuccess(fileDTO);
    }

    @ApiIgnore
    @GetMapping("/file-stream")
    @ResponseBody
    public void file() throws IOException {
        long fileId = (long)request.getAttribute(LinkController.FILE_ID);
        FilePO filePO = fileRepository.findById(fileId)
                .orElseThrow(()-> ClientException.client(ClientExceptionEnum.NOT_FOUND));

        File file = new File(filePO.getFilePath());
        if (!file.exists()) {
            LogUtil.error(log, "{}不存在", filePO.getFilePath());
            throw ClientException.client(ClientExceptionEnum.NOT_FOUND, "文件已失效", "文件路径不存在文件信息");
        }
        String originalFilename = filePO.getOriginalFilename();
        String mimeType = request.getServletContext().getMimeType(file.getName());
        response.setContentType(mimeType + ";charset=utf-8");
        /*
            attachment ： 以附件的形式进行下载
            inline:文本和图片会进行预览，其他会进行下载
         */
        String contentDisposition = new StringBuilder("inline;filename=")
                // 使用ISO8859-1的编码浏览器才能正确识别文件名
                .append(new String(originalFilename.getBytes(), "ISO8859-1"))
                .toString();
        response.setHeader("Content-Disposition", contentDisposition);
        response.setContentLengthLong(file.length());
        response.setCharacterEncoding("UTF-8");

        byte[] bytes = FileUtil.readBytes(file);
        response.getOutputStream().write(bytes);
    }

}