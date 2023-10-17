package com.goudong.authentication.server.service.manager;

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
     * @param fileName 文件名
     */
    void exportTemplateHandler(HttpServletResponse response, String fileName) throws IOException;


}
