package com.goudong.modules.simple;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/9/11 16:12
 */
public class FormatterDemo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        // System.out.printf("%s 今年 %d岁了", "Jack", 5);
        // System.out.format("%s 今年 %d岁了", "Jack", 5);

        // Formatter formatter = new Formatter(System.out);
        // formatter.format("%-15s %5s %10s\n", "item", "Qty", "Price");

        String format = String.format("%-15s %5s %10s\n", "item", "Qty", "Price");
        System.out.println(format);
    }
}
