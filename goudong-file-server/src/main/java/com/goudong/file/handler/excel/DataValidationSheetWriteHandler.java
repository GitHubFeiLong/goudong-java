package com.goudong.file.handler.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.goudong.commons.utils.core.SpringBeanTool;
import com.goudong.core.util.ArrayUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.file.annotation.excel.ExcelDataValidation;
import com.goudong.file.core.BasicCell;
import com.goudong.file.service.IExcelDataValidation;
import lombok.Data;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 数据校验，下拉
 * @author cfl
 * @version 1.0
 * @date 2022/11/23 10:38
 */
public class DataValidationSheetWriteHandler implements SheetWriteHandler {
    //~fields
    //==================================================================================================================
    private Class target;

    private List<Inner> inners;
    //~methods
    //==================================================================================================================
    public DataValidationSheetWriteHandler(Class target) {
        this.target = target;
        this.inners = parse();
    }
    //~methods
    //==================================================================================================================


    //~methods
    //==================================================================================================================
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();

        this.inners.stream().forEach(p -> {
            // 区间设置
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 65535, p.getCellIndex(), p.getCellIndex());
            DataValidationConstraint constraint = helper.createExplicitListConstraint(p.getDataValidation());
            DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);

            /*** 处理Excel兼容性问题 **/
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }

            sheet.addValidationData(dataValidation);
        });
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
                    ExcelDataValidation excelDataValidation = p.getAnnotation(ExcelDataValidation.class);
                    int index = excelProperty != null ? excelProperty.index() : -1;
                    int order = excelProperty != null ? excelProperty.order() : Integer.MAX_VALUE;
                    int fieldIndex = fieldIndexAtomic.getAndIncrement();
                    String[] dataValidation = getDataValidation(excelDataValidation);

                    Inner inner = new Inner(index, order, fieldIndex, p.getName(), dataValidation);
                    inners.add(inner);
                });

        // 处理cellIndex
        BasicCell.fillCellIndex(inners);

        // 只返回需要生成下拉的列
        return inners.stream().filter(f -> ArrayUtil.isNotEmpty(f.getDataValidation())).collect(Collectors.toList());
    }

    private String[] getDataValidation(ExcelDataValidation excelDataValidation) {
        if (excelDataValidation == null) {
            return new String[0];
        }
        if (excelDataValidation.value().length > 0) {
            return excelDataValidation.value();
        }

        if (excelDataValidation.source() != IExcelDataValidation.class) {
            Class<? extends IExcelDataValidation> source = excelDataValidation.source();
            try {
                // 从spring ioc中获取bean
                IExcelDataValidation bean = SpringBeanTool.getBean(source);
                return bean != null ?  bean.get() : source.newInstance().get();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return new String[0];
    }

    /**
     * 类描述：
     * 用于临时存储类对应的下拉信息
     * @author cfl
     * @version 1.0
     * @date 2022/11/22 19:01Inner */
    @Data
    public static class Inner extends BasicCell {
        //~fields
        //==================================================================================================================
        /**
         * 对应的下拉数据
         */
        private String[] dataValidation;
        //~methods
        //==================================================================================================================

        public Inner(int index, int order, int fieldIndex, String fieldName, String[] dataValidation) {
            super(index, order, fieldIndex, fieldName);
            this.dataValidation = dataValidation;
        }
    }
}
