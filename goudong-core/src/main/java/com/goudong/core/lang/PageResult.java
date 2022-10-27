package com.goudong.core.lang;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 本项目的同一分页结果封装
 * @author cfl
 * @date 2022/8/20 9:03
 * @version 1.0
 */
public class PageResult<T> {

    /**
     * 数据总条数
     */
    private Long total;

    /**
     * 数据总页数
     */
    private Long totalPage;

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 每页显示条目数
     */
    private Long size;

    /**
     * 分页结果
     */
    private List<T> content;

    public PageResult(List<T> content) {
        this.content = content;
    }

    public PageResult(Long total, Long totalPage, Long page, Long size, List<T> content) {
        this.total = total;
        this.totalPage = totalPage;
        this.page = page;
        this.size = size;
        this.content = content;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageResult<?> that = (PageResult<?>) o;
        return Objects.equals(total, that.total) && Objects.equals(totalPage, that.totalPage) && Objects.equals(page, that.page) && Objects.equals(size, that.size) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, totalPage, page, size, content);
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", page=" + page +
                ", size=" + size +
                ", content=" + content +
                '}';
    }
}
