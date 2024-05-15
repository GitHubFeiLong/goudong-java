package com.goudong.modules.simple.twelve;

/**
 * 类描述：
 *
 * @author chenf
 */
public class Demo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String ...args) {
        try {
            throw new RuntimeException("异常");
        } catch (Exception e) {
            for(StackTraceElement ele : e.getStackTrace()) {
                System.out.println("ele.getMethodName() = " + ele);
            }
        }
    }
}
