package com.goudong.file.controller.template;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.file.dto.UserExcelTemplateDTO;
import com.goudong.file.handler.excel.CommentRowWriteHandler;
import com.goudong.file.handler.excel.DataValidationSheetWriteHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

/**
 * 类描述：
 * 用户模块的相关模板
 * @author cfl
 * @version 1.0
 * @date 2022/11/20 8:41
 */
@Api(tags = "模板-用户模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/user-server/template")
@RequiredArgsConstructor
public class UserTemplateController {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @GetMapping(value = "/user")
    @ApiOperation(value = "获取用户模板")
    public void exportUserTemplate(HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("用户导入模板" + DateUtil.format(new Date(), DateConst.DATE_TIME_FORMATTER_SHORT) + ".xlsx", "UTF-8")
                .replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //attachment指定独立文件下载  不指定则回浏览器中直接打开
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        //导出excel
        EasyExcel.write(response.getOutputStream(), UserExcelTemplateDTO.class)
                .registerWriteHandler(new CommentRowWriteHandler(UserExcelTemplateDTO.class))
                .registerWriteHandler(new DataValidationSheetWriteHandler(UserExcelTemplateDTO.class))
                // .registerWriteHandler(new RequiredWriteHandler(UserExcelTemplateDTO.class))
                // .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet("用户导入")
                .doWrite(new ArrayList<>(1));
    }
}
