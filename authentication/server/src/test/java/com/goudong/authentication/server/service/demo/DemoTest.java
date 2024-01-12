package com.goudong.authentication.server.service.demo;

import org.junit.jupiter.api.Test;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
public class DemoTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testBit() {
        int b1 = 0xfe;
        System.out.println("b1 = " + b1);
        System.out.println(b1^1);
        System.out.println(b1^2);
        System.out.println(b1^3);
        System.out.println(b1^4);
    }
}
