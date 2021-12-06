package com.goudong.file.constant;

import java.io.File;
import java.util.Objects;

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
    public static final Boolean IS_WIN = Objects.equals(System.getProperty("sun.desktop"), "windows");
}
