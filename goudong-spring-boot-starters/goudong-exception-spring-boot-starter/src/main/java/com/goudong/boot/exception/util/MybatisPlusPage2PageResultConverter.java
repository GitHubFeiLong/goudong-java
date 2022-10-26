package com.goudong.boot.exception.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goudong.boot.exception.core.PageResult;
import com.goudong.boot.exception.core.PageResultConverter;

import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:19
 */
public class MybatisPlusPage2PageResultConverter<E> implements PageResultConverter<Page, PageResult, E> {

    @Override
    public PageResult<E> convert(Page source, Class<E> tClazz) {

        Long total = source.getTotal();
        Long page = source.getCurrent();
        Long size = source.getSize();
        List content = BeanUtil.copyToList(source.getRecords(), tClazz, CopyOptions.create());
        Long totalPage = (long)Math.ceil(total / size);

        return new PageResult<E>(total, totalPage, page, size,content);
    }

}
