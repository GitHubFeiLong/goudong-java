package com.goudong.junit5.demo.service;

import org.springframework.stereotype.Service;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/9/18 1:24
 */
@Service
public class BService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public int badd(int i1, int i2) {
        return i1 + i2;
    }
}
