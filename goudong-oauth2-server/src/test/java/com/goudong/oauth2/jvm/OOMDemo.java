package com.goudong.oauth2.jvm;

import com.google.common.collect.Lists;
import com.goudong.oauth2.po.BaseUserPO;

import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 20:01
 */
public class OOMDemo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * -Xms3m -Xmx3m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./
     *
     * 生成的文件在goudong-java目录下，使用jvisualvm载入查看
     * @param args
     */
    public static void main(String[] args) {
        List<BaseUserPO> oomList = Lists.newArrayList();
        // 无限循环创建对象
        while (true) {
            oomList.add(new BaseUserPO());
        }
    }
}
