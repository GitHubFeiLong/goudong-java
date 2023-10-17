package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.service.manager.ImportExportManagerService;
import com.goudong.boot.web.core.ClientException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * 类描述：
 * 导入导出管理服务接口实现
 * @author chenf
 * @version 1.0
 */
@Service
public class ImportExportManagerServiceImpl implements ImportExportManagerService {

    //~fields
    //==================================================================================================================
    public static final String PREFIX_DIR = "templates/";
    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    //~methods
    //==================================================================================================================
    public void exportTemplateHandler(String fileName) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream fileInputStream = loader.getResourceAsStream(PREFIX_DIR + fileName);

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        byte[] buffer = new byte[1024];
        int len;
        //4、执行写出操作
        while ((len = fileInputStream.read(buffer)) != -1){
            outputStream.write(buffer,0,len);
        }

        // 关闭输入流和输出流
        fileInputStream.close();
        outputStream.close();
    }
}
