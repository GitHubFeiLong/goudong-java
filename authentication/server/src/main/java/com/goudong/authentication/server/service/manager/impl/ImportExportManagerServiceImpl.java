package com.goudong.authentication.server.service.manager.impl;

import com.alibaba.excel.EasyExcel;
import com.goudong.authentication.server.easyexcel.listener.BaseUserImportExcelListener;
import com.goudong.authentication.server.rest.req.BaseUserImportReq;
import com.goudong.authentication.server.rest.resp.BaseImportResp;
import com.goudong.authentication.server.rest.resp.BaseUserImportResp;
import com.goudong.authentication.server.easyexcel.template.BaseUserImportExcelTemplate;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.ImportExportManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

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
    private TransactionTemplate transactionTemplate;

    @Resource
    private BaseUserService baseUserService;

    @Resource
    private PasswordEncoder passwordEncoder;

    //~methods
    //==================================================================================================================


    /**
     * 导出"resources/templates/"下指定文件（{@code fileName}）
     * @param response 响应对象
     * @param fileName 文件名
     */
    public void exportTemplateHandler(HttpServletResponse response, String fileName) throws IOException {
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

    /**
     * 导入用户
     *
     * @param req 导入参数
     * @return 导入结果
     */
    @Override
    public boolean importUser(BaseUserImportReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseUserImportExcelTemplate.class,
                    new BaseUserImportExcelListener(myAuthentication, baseUserService, transactionTemplate, passwordEncoder))
                    .sheet()
                    // 第二行开始解析
                    .headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
