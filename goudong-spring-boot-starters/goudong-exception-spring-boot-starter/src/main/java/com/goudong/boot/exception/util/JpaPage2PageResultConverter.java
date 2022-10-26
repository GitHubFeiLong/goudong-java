package com.goudong.boot.exception.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.boot.exception.core.PageResult;
import com.goudong.boot.exception.core.PageResultConverter;
import org.springframework.data.domain.Page;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:19
 */
public class JpaPage2PageResultConverter<E> implements PageResultConverter<Page, PageResult, E> {

    @Override
    public PageResult<E> convert(Page source, Class<E> tClazz) {
        return new PageResult<E>(source.getTotalElements(),
                (long) source.getTotalPages(),
                source.getPageable().getPageNumber() + 1L,
                (long) source.getPageable().getPageSize(),
                BeanUtil.copyToList(source.getContent(), tClazz, CopyOptions.create())
        );
    }

}
