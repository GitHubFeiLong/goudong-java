package com.goudong.file.service;

import com.goudong.commons.framework.openfeign.dto.BaseUser2QueryPageReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 接口描述：
 * 用户服务层接口
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface UserServerService {

    /**
     * 导出用户
     * @param req       导出条件
     * @param response  响应对象
     */
    void userExportExcel(BaseUser2QueryPageReq req, HttpServletResponse response) throws UnsupportedEncodingException;

    /**
     * 导入用户
     * @param file
     */
    void importUser(MultipartFile file);
}