package com.goudong.modules.easyexcel.demo.write;

import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/20 19:23
 */
@ExtendWith({})
public class WriteTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testTemplate() {
        String templateFileName = "template.xlsx";
        String fileName = System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, UserExcelTemplateDTO.class)
                .withTemplate(templateFileName)
                .sheet()
                .doWrite(new ArrayList<>());
    }
}
