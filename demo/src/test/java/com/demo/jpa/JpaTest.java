package com.demo.jpa;

import com.goudong.Application;
import com.goudong.boot.exception.core.PageResult;
import com.goudong.boot.exception.util.PageResultConvert;
import com.goudong.po.BaseTokenPO;
import com.goudong.repository.BaseTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 20:14
 */
@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
public class JpaTest {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseTokenRepository repository;
    //~methods
    //==================================================================================================================
    @Test
    void test1() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BaseTokenPO> all = repository.findAll(pageRequest);

        PageResult<BaseTokenPO> convert = PageResultConvert.convert(all, BaseTokenPO.class);
        System.out.println("convert = " + convert);
    }
}
