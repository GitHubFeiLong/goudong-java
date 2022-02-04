package com.goudong.commons.utils.core;

import java.io.*;

/**
 * 类描述：
 * IO 工具类, 读写文件
 * @author msi
 * @version 1.0
 * @date 2022/2/4 17:58
 */
public class IOUtil {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void writeFile(String data, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.flush();
        } finally {
            close(out);
        }
    }

    public static String readFile(File file) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            byte[] data = out.toByteArray();
            return new String(data);
        } finally {
            close(in);
            close(out);
        }
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                // nothing
            }
        }
    }

}