package com.goudong.file.core;

import com.alibaba.excel.annotation.ExcelProperty;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/22 21:59
 */
@Slf4j
@Data
public class BasicCell {
    //~fields
    //==================================================================================================================
    /**
     * 行下标
     */
    protected Integer rowIndex;
    /**
     * 根据{@code index}，{@code order}，{@code fieldIndex} 进行计算出来的列下表
     */
    protected Integer cellIndex;
    /**
     * @see ExcelProperty#index()
     */
    protected int index;

    /**
     * @see ExcelProperty#order()
     */
    protected int order;

    /**
     * java的属性字段排序
     */
    protected int fieldIndex;

    /**
     * 字段属性名
     */
    protected String fieldName;

    //~methods
    //==================================================================================================================

    /**
     * 填充 {@code cellIndex}
     */
    public static void fillCellIndex(List<? extends BasicCell> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            // 未使用的索引
            List<Integer> unusedCellIndex = CollectionUtil.newArrayListByRange(0, list.size() - 1);
            /*
                处理 index
             */
            // index != -1 时 以 index为准（index不能重复）
            list.stream().filter(f -> f.getCellIndex() == null && f.getIndex() != -1).forEach(p -> {
                p.setCellIndex(p.getIndex());
                // 移除未使用
                unusedCellIndex.remove(Integer.valueOf(p.getIndex()));
            });

            // 排序
            Collections.sort(unusedCellIndex);

            /*
                处理order
             */
            list.stream()
                    // order != Integer.MAX_VALUE 时 以 order为准（order可以重复，重复时以字段声明顺序显示）
                    .filter(f -> f.getCellIndex() == null && f.getOrder() != Integer.MAX_VALUE)
                    // order 升序,fieldIndex 升序
                    .sorted(Comparator.comparing(BasicCell::getOrder).thenComparing(BasicCell::getFieldIndex))
                    .forEach(p -> {
                        // 可用的cellIndex的最小值
                        Integer minCellIndex = unusedCellIndex.get(0);
                        p.setCellIndex(minCellIndex);
                        // 移除
                        unusedCellIndex.remove(Integer.valueOf(minCellIndex));
                    });

            // 排序
            Collections.sort(unusedCellIndex);

            /*
                处理 默认排序
             */
            list.stream()
                    // 剩余的都是没有加排序的字段
                    .filter(f -> f.getCellIndex() == null)
                    // order 升序
                    .sorted(Comparator.comparing(BasicCell::getFieldIndex))
                    .forEach(p -> {
                        // 可用的cellIndex的最小值
                        Integer minCellIndex = unusedCellIndex.get(0);
                        p.setCellIndex(minCellIndex);
                        // 移除
                        unusedCellIndex.remove(Integer.valueOf(minCellIndex));
                    });

            /*
                判断是否未处理完
             */
            List<? extends BasicCell> collect = list.stream().filter(f -> f.getCellIndex() == null).collect(Collectors.toList());
            AssertUtil.isEmpty(collect, () -> {
                log.error("还有数据未处理完 cellIndex：{}", collect);
                return new RuntimeException("还有数据未处理完 cellIndex");
            });

        }
    }

    public BasicCell() {

    }

    public BasicCell(int index, int order, int fieldIndex, String fieldName) {
        this.index = index;
        this.order = order;
        this.fieldIndex = fieldIndex;
        this.fieldName = fieldName;
    }

    public BasicCell(int rowIndex, int index, int order, int fieldIndex, String fieldName) {
        this.rowIndex = rowIndex;
        this.index = index;
        this.order = order;
        this.fieldIndex = fieldIndex;
        this.fieldName = fieldName;
    }
}
