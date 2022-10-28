package com.goudong.boot.web.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goudong.boot.web.core.PageResultConverter;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.MessageFormatUtil;

import java.util.List;

/**
 * 类描述：
 * Mybatis Plus的分页结果转换器
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:19
 */
final class MybatisPlusPage2PageResultConverter<E> implements PageResultConverter<Page, PageResult, E> {
    /**
     * 实例
     */
    public static MybatisPlusPage2PageResultConverter instance;

    /**
     * 获取实例
     * @return
     */
    public static MybatisPlusPage2PageResultConverter getInstance() {
        if (instance == null) {
            synchronized(MybatisPlusPage2PageResultConverter.class){
                if (instance == null) {
                    instance = new MybatisPlusPage2PageResultConverter();
                }
            }
        }
        return instance;
    }

    @Override
    public PageResult<E> basicConvert(Page source, Class<E> tClazz) {

        Long total = source.getTotal();
        Long page = source.getCurrent();
        Long size = source.getSize();
        List content = BeanUtil.copyToList(source.getRecords(), tClazz, CopyOptions.create());
        Long totalPage = (long)Math.ceil(total / size);

        return new PageResult<E>(total, totalPage, page, size,content);
    }

    @Override
    public PageResult convert(Object source, Class<E> tClazz) {
        if (source instanceof Page) {
            return basicConvert((Page)source, tClazz);
        }
        throw new IllegalArgumentException(MessageFormatUtil.format("参数{}不是{}类型", source, Page.class));
    }

    private MybatisPlusPage2PageResultConverter(){

    }
}
