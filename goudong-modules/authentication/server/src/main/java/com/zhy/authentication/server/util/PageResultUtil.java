package com.zhy.authentication.server.util;

import cn.zhxu.bs.SearchResult;
import com.goudong.boot.web.core.PageResultConverter;
import com.goudong.core.lang.PageResult;
import com.zhy.authentication.server.rest.req.search.BasePage;

public class PageResultUtil {

    private static SearchResult2PageResultConverter converter;

    private PageResultUtil() {

    }

    public static SearchResult2PageResultConverter getConverter() {
        if (converter != null) {
            return converter;
        }
        synchronized (PageResultUtil.class) {
            if (converter != null) {
                return converter;
            }
            converter = new SearchResult2PageResultConverter();
            return converter;
        }
    }

    /**
     * 转分页结果
     * @param source
     * @param basePage
     * @return
     */
    public static PageResult convert(SearchResult source, BasePage basePage) {
        PageResult pageResult = getConverter().basicConvert(source, null);
        pageResult.setPage(basePage.getPage().longValue() + 1);
        pageResult.setSize(basePage.getSize().longValue());
        // 总页数
        Long totalPage = pageResult.getTotal() == 0 ? 0L : (long)Math.floor(pageResult.getTotal() / basePage.getSize()) + 1;
        pageResult.setTotalPage(totalPage);
        return pageResult;
    }

    public static class SearchResult2PageResultConverter<E> implements PageResultConverter<SearchResult, PageResult, E> {
        @Override
        public PageResult basicConvert(SearchResult source, Class<E> tClazz) {
            return new PageResult(source.getTotalCount().longValue(), 0L, 0L, 0L, source.getDataList());
        }

        @Override
        public PageResult convert(Object source, Class<E> tClazz) {
            return null;
        }
    }
}
