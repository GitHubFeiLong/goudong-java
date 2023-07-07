package com.goudong.file.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.framework.openfeign.GoudongUserServerService;
import com.goudong.commons.framework.openfeign.dto.BaseUser2QueryPageReq;
import com.goudong.commons.framework.openfeign.dto.BaseUser2QueryPageResp;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import com.goudong.file.dto.BaseUserExportDTO;
import com.goudong.file.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户服务层
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaseUserServiceImpl implements BaseUserService {

    private final GoudongUserServerService goudongUserServerService;

    /**
     * 导出用户
     *
     * @param req      导出条件
     * @param response 响应对象
     * @return
     */
    @Override
    public void userExportExcel(BaseUser2QueryPageReq req, HttpServletResponse response)  {
        // 方法1: 如果写到同一个sheet
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("用户导出" + DateUtil.format(new Date(), DateConst.DATE_TIME_FORMATTER_SHORT) + ".csv", "UTF-8")
                    .replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //attachment指定独立文件下载  不指定则回浏览器中直接打开
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BaseUserExportDTO.class)
                .registerConverter(new LongStringConverter())
                .build()
        ) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet01").build();
            /*
                分页写入excel，对内存友好
             */
            // 分页查询
            // 每次查询100条
            req.setSize(100);
            // 初始分页设置0
            int page = 0;
            // 总店铺数量
            long total = 0;
            AtomicLong atomicLong = new AtomicLong(1);
            do {
                // 页码加一
                page += 1;
                req.setPage(page);
                Result<PageResult<BaseUser2QueryPageResp>> pageResult = goudongUserServerService.pageUser(req);
                // 分页查询
                // 获取总数量
                total = pageResult.getData().getTotal();

                List<BaseUserExportDTO> data = new ArrayList<>(pageResult.getData().getContent().size());

                pageResult.getData().getContent().stream().forEach(p -> {
                    BaseUserExportDTO dto = new BaseUserExportDTO();
                    dto.setSerialNumber(atomicLong.getAndIncrement());
                    dto.setUsername(p.getUsername());
                    dto.setNickname(p.getNickname());
                    dto.setSex(p.getSex() == 0 ? "未知" : (p.getSex() == 1 ? "男性" : "女性"));
                    dto.setPhone(p.getPhone());
                    dto.setEmail(p.getEmail());
                    dto.setRoleNameCn(p.getRoles().stream().map(m -> m.getRoleNameCn()).collect(Collectors.joining(",")));
                    dto.setRemark(p.getRemark());
                    dto.setValidTime(p.getValidTime());
                    dto.setCreateTime(p.getCreateTime());
                    dto.setEnabled(p.getEnabled() ? "激活" : "未激活");
                    dto.setLocked(p.getLocked() ? "锁定" : "未锁定");

                    data.add(dto);
                });

                // 调用写入
                excelWriter.write(data, writeSheet);
            } while ((page * req.getSize()) < total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
