package com.goudong.file.handler;

import com.goudong.core.util.CollectionUtil;
import com.goudong.file.dto.UserExcelTemplateDTO;
import com.goudong.file.handler.excel.CommentRowWriteHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith({})
class CommentRowWriteHandlerTest {

    @Test
    void test1() {
        new CommentRowWriteHandler(UserExcelTemplateDTO.class);
    }

    @Test
    void test2() {
        List<Integer> integers = CollectionUtil.newArrayListByRange(0, 10);
        System.out.println("integers = " + integers);
        boolean remove = integers.remove(Integer.valueOf(10));
        System.out.println("remove = " + remove);
        System.out.println("integers = " + integers);
    }
}