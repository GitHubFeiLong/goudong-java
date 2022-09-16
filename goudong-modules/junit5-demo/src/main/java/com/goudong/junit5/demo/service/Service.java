package com.goudong.junit5.demo.service;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/9/16 11:18
 */
@org.springframework.stereotype.Service
public class Service {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public int add(int i1, int i2) {
        return i1+i2;
    }

    public int sub(int i1, int i2) {
        return i1-i2;
    }

    public Service() {
    }
}