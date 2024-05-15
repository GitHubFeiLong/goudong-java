package com.goudong.modules.simple.eleven;

import java.util.*;

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
    public static void main(String[] args) {
        Stack<Character> stack = new Stack<>();
        stack.push('U');
        stack.push('n');
        stack.push('c');
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push('e');
        stack.push('r');
        stack.push('t');
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push('a');
        System.out.println(stack.pop());
        stack.push('i');
        System.out.println(stack.pop());
        stack.push('n');
        stack.push('t');
        stack.push('y');
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push('~');
        stack.push('r');
        stack.push('u');
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        stack.push('l');
        stack.push('e');
        stack.push('s');
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }
}
