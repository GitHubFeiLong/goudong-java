package com.goudong.file.handler.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.constant.OrderConstant;
import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.goudong.core.util.AssertUtil;
import com.goudong.file.annotation.excel.ExcelRequired;
import com.goudong.file.core.BasicCell;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

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
public class RequiredWriteHandler implements CellWriteHandler {
    //~fields
    //==================================================================================================================
    private Class target;

    private List<Inner> inners;
    //~methods
    //==================================================================================================================
    public RequiredWriteHandler(Class target) {
        this.target = target;
        this.inners = parse();
    }

    /**
     * handler order
     *
     * @return order
     */
    @Override
    public int order() {
        return  OrderConstant.DEFINE_STYLE;
    }

    /**
     * Called after all operations on the cell have been completed
     *
     * @param context
     */
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (context.getHead()) {
            Cell cell = context.getCell();
            WriteContext writeContext = context.getWriteContext();
            WriteWorkbookHolder writeWorkbookHolder = writeContext.writeWorkbookHolder();
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            this.inners.stream().filter(f -> f.getCellIndex().equals(cell.getColumnIndex())).forEach(p -> {
                // 加上 *
                String content = "*" + cell.getRichStringCellValue();
                cell.setCellValue(content);
                //
                // CellStyle headWriteCellStyle = workbook.createCellStyle();
                // headWriteCellStyle.setWrapText(true);
                // headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                // headWriteCellStyle.setLocked(true);
                // // 设置前景背景
                // headWriteCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // // 设置前景背景颜色
                // headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                // headWriteCellStyle.setBorderTop(BorderStyle.THIN);
                // headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
                // headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
                // headWriteCellStyle.setBorderRight(BorderStyle.THIN);
                // Font font = workbook.createFont();
                // font.setFontName("宋体");
                // font.setFontHeightInPoints((short)14);
                // font.setColor(Font.COLOR_RED);
                // font.setBold(true);
                // headWriteCellStyle.setFont(font);
                // cell.setCellStyle(headWriteCellStyle);
                // TODO 富文本在表头设置没有效果展
                // XSSFRichTextString xssfRichTextString = new XSSFRichTextString(content);
                // xssfRichTextString.applyFont(font);
                // xssfRichTextString.applyFont(0, 1, (short)30);
                // xssfRichTextString.applyFont(1, content.length() - 1,  (short)10);
                // RichTextString richStringCellValue = cell.getRichStringCellValue();
                // richStringCellValue.applyFont(0, 1, (short)30);
                // richStringCellValue.applyFont(1, content.length() - 1,  (short)10);
                // cell.setCellValue(xssfRichTextString);
                // context.getFirstCellData().setWriteCellStyle(null);
            });
        }
    }

    /**
     * 解析注解
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
                    ExcelRequired excelRequired = p.getAnnotation(ExcelRequired.class);
                    int index = excelProperty != null ? excelProperty.index() : -1;
                    int order = excelProperty != null ? excelProperty.order() : Integer.MAX_VALUE;
                    int fieldIndex = fieldIndexAtomic.getAndIncrement();
                    boolean required = excelRequired != null;

                    Inner inner = new Inner(0, index, order, fieldIndex, p.getName(), required);
                    inners.add(inner);
                });

        // 处理cellIndex
        BasicCell.fillCellIndex(inners);

        // 只返回需要生成批注的列
        return inners.stream().filter(Inner::isRequired).collect(Collectors.toList());
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
         * 是否是必填
         */
        private boolean required;
        //~methods
        //==================================================================================================================

        public Inner(int rowIndex, int index, int order, int fieldIndex, String fieldName, boolean required) {
            super(rowIndex, index, order, fieldIndex, fieldName);
            this.required = required;
        }
    }
}
