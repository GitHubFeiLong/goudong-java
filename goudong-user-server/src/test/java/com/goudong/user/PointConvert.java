package com.goudong.user;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.awt.*;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/9/15 21:03
 */
public class PointConvert implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (source != null) {
            String[] arr = String.valueOf(source).split("/");
            if (arr.length == 2) {
                try {
                    int x = Integer.parseInt(arr[0]);
                    int y = Integer.parseInt(arr[1]);
                    return new Point(x, y);
                } catch (NumberFormatException e) {
                    throw new ArgumentConversionException("格式正确，但是坐标不是数字");
                }
            }
            throw new ArgumentConversionException("参数格式不正确,正确的格式为x/y");
        }
        throw new ArgumentConversionException("参数不能为空");
    }

}
