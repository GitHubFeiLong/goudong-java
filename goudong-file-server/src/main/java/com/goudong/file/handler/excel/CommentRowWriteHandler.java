package com.goudong.file.handler.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import com.goudong.file.annotation.excel.ExcelComment;
import com.goudong.file.core.BasicCell;
import lombok.Data;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/21 21:42
 */
public class CommentRowWriteHandler implements RowWriteHandler {
    //~fields
    //==================================================================================================================
    private Class target;

    private List<Inner> inners;
    //~methods
    //==================================================================================================================
    public CommentRowWriteHandler(Class target) {
        this.target = target;
        this.inners = parse();
    }

    /**
     * Called after the row is created
     *
     * @param writeSheetHolder
     * @param writeTableHolder Nullable.It is null without using table writes.
     * @param row
     * @param relativeRowIndex Nullable.It is null in the case of fill data.
     * @param isHead           Nullable.It is null in the case of fill data.
     */
    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (isHead) {
            Sheet sheet = writeSheetHolder.getSheet();
            Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
            // 需要添加批注的单元格
            this.inners.stream().forEach(p -> {
                // 列的宽度（英文字符的长度）
                int columnWidth = sheet.getColumnWidth(p.getCellIndex()) / 256;
                // 批注的长度
                int commentLength = p.getComment().length();
                // 计算所占的行数 （x 2 是中文占的宽度比英文宽）
                int rowNum = (int)Math.ceil(commentLength * 2.0 / columnWidth);

                // 在第一行 第二列创建一个批注
                Comment comment = drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                        // 需要创建索引的单元格列索引
                        p.getCellIndex(),
                        // 对应的行
                        0,
                        // 向右横跨一个单元格
                        p.getCellIndex() + 1,
                        // 对应的高度占到行索引
                        rowNum
                    ));
                // 输入批注信息
                comment.setString(new XSSFRichTextString(p.getComment()));
                // 将批注添加到单元格对象中
                sheet.getRow(p.getRowIndex()).getCell(p.getCellIndex()).setCellComment(comment);
            });
        }
    }

    /**
     * 解析注解{@code ExcelComment}
     * @return
     */
    @SuppressWarnings("all")
    private List<Inner> parse() {
        AssertUtil.isNotNull(this.target);
        // 没有 @ExcelProperty的字段不会生成
        boolean isIgnoreUnannotated =  this.target.getAnnotation(ExcelIgnoreUnannotated.class) != null;
        // 获取定义的所有字段
        Field[] declaredFields = this.target.getDeclaredFields();
        // java字段
        AtomicInteger fieldIndexAtomic = new AtomicInteger(0);
        List<Inner> inners = new ArrayList<>(declaredFields.length);
        Stream.of(declaredFields)
                .filter(f -> {
                     /*
                        这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。
                        优先级：index > order > java属性默认排序
                        index 不能重复；order 能重复，重复时以字段声明的顺序排序
                     */
                    ExcelProperty excelProperty = f.getAnnotation(ExcelProperty.class);
                    ExcelIgnore excelIgnore = f.getAnnotation(ExcelIgnore.class);
                    // 需要处理的情况
                    return excelIgnore == null && (isIgnoreUnannotated ? excelProperty != null : true);
                })
                .forEach(p -> {
                    ExcelProperty excelProperty = p.getAnnotation(ExcelProperty.class);
                    ExcelComment excelComment = p.getAnnotation(ExcelComment.class);
                    int index = excelProperty != null ? excelProperty.index() : -1;
                    int order = excelProperty != null ? excelProperty.order() : Integer.MAX_VALUE;
                    int fieldIndex = fieldIndexAtomic.getAndIncrement();
                    String comment = excelComment != null ? excelComment.value() : null;

                    Inner inner = new Inner(0, index, order, fieldIndex, p.getName(), comment);
                    inners.add(inner);
                });

        // 处理cellIndex
        BasicCell.fillCellIndex(inners);

        // 只返回需要生成批注的列
        return inners.stream().filter(f -> StringUtil.isNotBlank(f.getComment())).collect(Collectors.toList());
    }

    /**
     * 类描述：
     * 用于临时存储类对应的批注信息
     * @author cfl
     * @version 1.0
     * @date 2022/11/22 19:01Inner */
    @Data
    public static class Inner extends BasicCell {
        //~fields
        //==================================================================================================================
        /**
         * 对应的批注
         */
        private String comment;
        //~methods
        //==================================================================================================================

        public Inner(int rowIndex, int index, int order, int fieldIndex, String fieldName, String comment) {
            super(rowIndex, index, order, fieldIndex, fieldName);
            this.comment = comment;
        }
    }
}
