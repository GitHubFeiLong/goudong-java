package com.goudong.modules.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 类描述：
 * 客户端
 * @author cfl
 * @version 1.0
 * @date 2023/4/20 14:00
 */
public class Client {
    public static void main(String[] args) throws IOException {
        // 针对服务器指定对端口创建通讯
        Socket socket = new Socket("localhost", 9090);
        System.out.println("连接服务端成功，开始发送数据");
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        while (true) {
            /*
                发送消息到服务端
             */
            System.out.print("send: ");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();
            outputStream.write(s.getBytes());
            outputStream.flush();
            // 读取服务端发送的数据
            byte[] bytes = new byte[1024];
            int len;
            // 这里的 inputStream.read()方法会阻塞
            while ((len = inputStream.read(bytes)) != -1) {
                String serverMessage = new String(bytes, 0, len);
                System.out.println("server: " + serverMessage);
                break;
            }

            if ("bye".equals(s)) {
                System.out.println("关闭连接");
                inputStream.close();
                outputStream.close();
                socket.close();
                break;
            }
        }

    }
}
