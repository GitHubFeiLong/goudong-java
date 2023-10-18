package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.BaseUserImportReq;
import com.goudong.authentication.server.rest.resp.BaseImportResp;
import com.goudong.authentication.server.rest.resp.BaseUserImportResp;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 导入导出管理服务接口
 * @author chenf
 * @version 1.0
 */
public interface ImportExportManagerService {

    /**
     * 导出"resources/templates/"下指定文件（{@code fileName}）
     * @param response 响应对象
     * @param fileName 文件名
     */
    void exportTemplateHandler(HttpServletResponse response, String fileName) throws IOException;


    /**
     * 导入用户
     * @param req 导入参数
     * @return 导入结果
     */
    boolean importUser(BaseUserImportReq req);
}
