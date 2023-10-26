package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.goudong.authentication.server.easyexcel.listener.BaseRoleImportExcelListener;
import com.goudong.authentication.server.easyexcel.listener.BaseUserImportExcelListener;
import com.goudong.authentication.server.easyexcel.template.BaseRoleImportExcelTemplate;
import com.goudong.authentication.server.easyexcel.template.BaseUserExportTemplate;
import com.goudong.authentication.server.enums.option.ActivateEnum;
import com.goudong.authentication.server.enums.option.LockEnum;
import com.goudong.authentication.server.rest.req.*;
import com.goudong.authentication.server.rest.resp.BaseImportResp;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserImportResp;
import com.goudong.authentication.server.easyexcel.template.BaseUserImportExcelTemplate;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.ImportExportManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.core.lang.IEnum;
import com.goudong.core.lang.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 导入导出管理服务接口实现
 * @author chenf
 * @version 1.0
 */
@Slf4j
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
    private BaseRoleService baseRoleService;

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
    public Boolean importUser(BaseUserImportReq req) {
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

    /**
     * 导出用户
     *
     * @param response 响应
     * @param req      查询参数
     * @return 导出结果
     */
    @Override
    public void exportUser(HttpServletResponse response, BaseUserExportReq req) {
        log.info("开始执行用户导出");
        String fileName = "导出用户" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BaseUserExportTemplate.class)
                .registerConverter(new LongStringConverter())
                .build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet01").build();
            /*
                分页写入excel，对内存友好
             */
            // 分页查询
            BaseUserPageReq pageReq = req.getPageReq();
            // 每次查询100条
            pageReq.setSize(100);
            // 初始分页设置0
            int page = 0;
            // 总店铺数量
            long total = 0;
            AtomicInteger sequenceNumber = new AtomicInteger(1);
            do {
                List<BaseUserExportTemplate> result = new ArrayList<>();
                // 页码加一
                page += 1;
                pageReq.setPage(page);
                // 分页查询(展开的)
                PageResult<BaseUserPageResp> pageResult = baseUserService.page(pageReq);
                // 获取总数量
                total = pageResult.getTotal();

                // 将数据放入resule中
                pageResult.getContent().forEach(p -> {
                    BaseUserExportTemplate resp = new BaseUserExportTemplate();
                    resp.setSequenceNumber(sequenceNumber.getAndIncrement());
                    resp.setUsername(p.getUsername());
                    resp.setRoles(p.getRoles().stream().map(BaseRoleDropDownResp::getName).collect(Collectors.joining()));
                    resp.setValidTime(p.getValidTime());
                    resp.setEnabled(IEnum.getById(ActivateEnum.class, p.getEnabled()).getLabel());
                    resp.setLocked(IEnum.getById(LockEnum.class, p.getLocked()).getLabel());
                    resp.setRemark(p.getRemark());
                    resp.setCreatedDate(p.getCreatedDate());
                    result.add(resp);
                });

                // 调用写入
                excelWriter.write(result, writeSheet);
            } while (((long) page * pageReq.getSize()) < total);

        } catch (IOException e) {
            throw new RuntimeException("导出用户失败：" + e.getMessage(), e);
        }
    }

    /**
     * 导出角色
     *
     * @param req
     * @return
     */
    @Override
    public Boolean importRole(BaseRoleImportReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseRoleImportExcelTemplate.class,
                            new BaseRoleImportExcelListener(myAuthentication, baseRoleService, transactionTemplate))
                    .sheet()
                    // 第二行开始解析
                    .headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 导入菜单
     *
     * @param req
     * @return
     */
    @Override
    public Boolean importMenu(BaseMenuImportReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        try {
            EasyExcel.read(req.getFile().getInputStream(), BaseRoleImportExcelTemplate.class,
                            new BaseRoleImportExcelListener(myAuthentication, baseRoleService, transactionTemplate))
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
