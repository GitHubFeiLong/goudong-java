package com.goudong.boot.web.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.boot.web.core.PageResultConverter;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.MessageFormatUtil;
import org.springframework.data.domain.Page;

/**
 * 类描述：
 * spring data page 的分页结果转换器
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:19
 */
final class SpringDataPage2PageResultConverter<E> implements PageResultConverter<Page, PageResult, E> {

    /**
     * 实例
     */
    public static SpringDataPage2PageResultConverter instance;

    /**
     * 获取实例
     * @return
     */
    public static SpringDataPage2PageResultConverter getInstance() {
        if (instance == null) {
            synchronized(SpringDataPage2PageResultConverter.class){
                if (instance == null) {
                    instance = new SpringDataPage2PageResultConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public PageResult<E> basicConvert(Page source, Class<E> tClazz) {
        return new PageResult<E>(source.getTotalElements(),
                (long) source.getTotalPages(),
                source.getPageable().getPageNumber() + 1L,
                (long) source.getPageable().getPageSize(),
                BeanUtil.copyToList(source.getContent(), tClazz, CopyOptions.create())
        );
    }

    @Override
    public PageResult convert(Object source, Class<E> tClazz) {
        if (source instanceof Page) {
            return basicConvert((Page)source, tClazz);
        }
        throw new IllegalArgumentException(MessageFormatUtil.format("参数{}不是{}类型", source, Page.class));
    }

    private SpringDataPage2PageResultConverter() {

    }

}
