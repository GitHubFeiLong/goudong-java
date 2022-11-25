package com.goudong.file.controller.export;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.file.dto.BaseUser2QueryPageDTO;
import com.goudong.file.dto.BaseUserExportDTO;
import com.goudong.file.dto.BaseUserExportReq;
import com.goudong.file.service.BaseUserService;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户模块的相关导出
 * @author cfl
 * @version 1.0
 * @date 2022/11/7 15:31
 */
@Api(tags = "导出-用户模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/user/export")
@RequiredArgsConstructor
public class UserExportController {

    private final BaseUserService userService;

    @GetMapping(value = "/user")
    @ApiOperation(value = "导出用户", notes = "参数跟分页参数基本一致")
    public void exportExcel(BaseUserExportReq req, HttpServletResponse response) throws IOException {
        //获取数据
        List<BaseUserDTO> content;
        if (CollectionUtil.isNotEmpty(req.getIds())) {
            content = userService.findAllById(req.getIds()).stream()
                    .sorted(Comparator.comparing(com.goudong.commons.dto.oauth2.BaseUserDTO::getCreateTime).reversed())
                    .collect(Collectors.toList());
        } else {
            BaseUser2QueryPageDTO pageDTO = BeanUtil.copyProperties(req, BaseUser2QueryPageDTO.class);
            content = userService.export(pageDTO);
        }

        List<BaseUserExportDTO> data = BeanUtil.copyToList(content, BaseUserExportDTO.class, CopyOptions.create());
        AtomicLong atomicLong = new AtomicLong(1);
        data.stream().forEach(p->{
            p.setSerialNumber(atomicLong.getAndIncrement());
            String collect = p.getRoles().stream().map(m -> m.getRoleNameCn()).collect(Collectors.joining(","));
            p.setRoleNameCn(collect);
        });
        String fileName = URLEncoder.encode("用户导出" + DateUtil.format(new Date(), DateConst.DATE_TIME_FORMATTER_SHORT) + ".xlsx", "UTF-8")
                .replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //attachment指定独立文件下载  不指定则回浏览器中直接打开
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        //导出excel
        EasyExcel.write(response.getOutputStream(), BaseUserExportDTO.class).sheet("用户").doWrite(data);
    }
}
