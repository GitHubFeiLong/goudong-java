package com.goudong.file.handler.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.Data;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.Map;

/**
 * 类描述：
 * 下拉处理器
 * @author cfl
 * @version 1.0
 * @date 2022/11/21 10:09
 */
@Data
public class DropDownHandler implements SheetWriteHandler {
    //~fields
    //==================================================================================================================
    /**
     * 从第几行开始，默认2行（index从0开始，0是标题）
     */
    private Integer firstRowIndex = 1;
    /**
     * 多少行有下拉,默认200行
     */
    private Integer rowSize = 200;

    /**
     * 下拉数据
     * <pre>
     *     key      ->  colIndex 第几列，从0开始
     *     value    ->  下拉内容
     * </pre>
     */
    private Map<Integer, String[]> colMap;
    //~methods
    //==================================================================================================================


    //~methods
    //==================================================================================================================
    public DropDownHandler() {

    }

    /**
     * 最简单的构造方法，{@code firstRowIndex}和{@code rowSize}使用缺省值
     * @param colMap 下拉数据
     */
    public DropDownHandler(Map<Integer, String[]> colMap) {
        this.colMap = colMap;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (colMap == null || colMap.isEmpty()) {
            return;
        }

        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();

        colMap.forEach((colIndex, value) -> {
            // 区间设置
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRowIndex, 65535, colIndex, colIndex);
            DataValidationConstraint constraint = helper.createExplicitListConstraint(value);
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
}
