package com.goudong.commons.constant.core;

import com.goudong.commons.utils.core.AssertUtil;

import java.io.File;

/**
 * 类描述：
 * 系统环境变量常量,简化System.getenv()
 * @author msi
 * @version 1.0
 * @date 2021/12/6 22:21
 */
public class SystemEnvConst {
    /**
     * LOCALAPPDATA -> C:\Users\16967\AppData\Local
     */
    public static final String LOCALAPPDATA = System.getenv("LOCALAPPDATA");

    /**
     * Windows -> C:\Users\16967
     * Linux -> /root
     */
    public static final String USER_HOME = System.getProperty("user.home");
    /**
     * 当前类路径所在目录
     * user.dir -> D:\workspaces\github-repository\goudong-java
     */
    public static final String USER_DIR = System.getProperty("user.dir");
    /**
     * windows: \
     * linux: /
     */
    public static final String SEPARATOR = File.separator;

    /**
     * 是否是windows系统
     */
    public static final Boolean IS_WIN;

    static {
        String OSName = System.getProperty("os.name").toLowerCase();
        final String windows = "windows";
        int indexOf = OSName.indexOf(windows);
        if (indexOf != -1) {
            IS_WIN = true;
        } else {
            IS_WIN = false;
        }


    }

    public static void main(String[] args) {
        // System.out.println("File.separator = " + File.separator);
        // Properties properties = System.getProperties();
        // properties.list(System.out);
        // // File.listRoots();
        // System.out.println("===================");
        // Map<String, String> getenv = System.getenv();
        // getenv.forEach((k,v)->{
        //     System.out.println(k + " = " + v);
        // });

        AssertUtil.isDiskPath("C:\\", "");
    }
}
