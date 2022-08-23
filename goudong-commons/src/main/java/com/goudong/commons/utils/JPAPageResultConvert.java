package com.goudong.commons.utils;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.commons.utils.core.BeanUtil;
import org.springframework.data.domain.Page;

/**
 * 类描述：
 * 将spring data jpa 的分页结果，转换成项目统一的分页结果。
 * @author cfl
 * @version 1.0
 * @date 2022/8/20 9:01
 */
public class JPAPageResultConvert {
    //~fields
    //==================================================================================================================


    //~methods
    //==================================================================================================================
    private JPAPageResultConvert(){

    }

    /**
     * 转成统一的格式，注意的是jpa的页码是以0开头，所以这里在返回时需要将其加1
     * @param page
     * @return
     * @param <T>
     */
    public static <T> BasePageResult<T> convert(Page<T> page) {
        return new BasePageResult(page.getTotalElements(),
                (long)page.getTotalPages(),
                page.getPageable().getPageNumber() + 1L,
                (long)page.getPageable().getPageSize(),
                page.getContent()
        );
    }

    /**
     * 转成统一的格式，注意的是jpa的页码是以0开头，所以这里在返回时需要将其加1
     * @param page 分页结果
     * @param target 转换成指定数据
     * @return
     */
    public static <T> BasePageResult<T> convert(Page page, Class<T> target) {
        return new BasePageResult(page.getTotalElements(),
                (long) page.getTotalPages(),
                page.getPageable().getPageNumber() + 1L,
                (long) page.getPageable().getPageSize(),
                BeanUtil.copyToList(page.getContent(), target, CopyOptions.create())
        );
    }
}
