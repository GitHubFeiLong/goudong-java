package com.goudong.boot.exception.core;

import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 本项目的同一分页结果封装
 * @author cfl
 * @date 2022/8/20 9:03
 * @version 1.0
 */
@Data
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

}
