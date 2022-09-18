package com.goudong.junit5.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/9/16 11:18
 */
@Service
public class AService {

    //~fields
    //==================================================================================================================
    @Autowired
    BService bService;

    //~methods
    //==================================================================================================================
    public int add(int i1, int i2) {
        System.out.println("真实方法被调用");
        return i1+i2;
    }

    public int badd(int i1, int i2) {
        System.out.println("badd 卡");
        return bService.badd(i1, i2);
    }

    public int sub(int i1, int i2) {
        return i1-i2;
    }

}