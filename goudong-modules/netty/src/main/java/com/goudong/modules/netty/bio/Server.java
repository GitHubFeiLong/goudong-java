package com.goudong.modules.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类描述：
 * BIO 服务端
 * @author cfl
 * @version 1.0
 * @date 2023/4/20 9:11
 */
public class Server {

    // 在线人数
    static AtomicInteger onlineNum = new AtomicInteger(0);

    // 线程池
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(4,
            20,
            10,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            // 服务指定端口，启动服务
            serverSocket = new ServerSocket(9090);

            // 死循环
            while (true) {
                System.out.println(String.format("等待客户端连接....."));
                Socket socket = serverSocket.accept();
                onlineNum.getAndIncrement();
                System.out.println(String.format("客户端连接成功(当前在线人数：%s)", onlineNum.get()));
                executor.submit(new Task(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            serverSocket.close();
        }
    }

    public static class Task implements Runnable {
        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                // 处理读写
                InputStream inputStream = socket.getInputStream();      // 读取客户端发送的数据。
                OutputStream outputStream = socket.getOutputStream();   // 发送给客户端发送数据。
                int len;
                byte[] bytes = new byte[1024];
                // inputStream.read() 执行会进行阻塞，直到读取到内容才向下执行。
                while((len = inputStream.read(bytes)) != -1) {
                    // 读取客户端发送来的数据
                    String clientMessage = new String(bytes, 0, len);
                    System.out.println("客户端:" + clientMessage);

                    if ("bye".equals(clientMessage)) {
                        outputStream.write("欢迎再来".getBytes());
                        outputStream.flush();
                        System.out.println("客户端关闭连接");
                        onlineNum.decrementAndGet();
                        break;
                    } else {
                        // 给客户端回消息
                        outputStream.write(("接收到消息 " + clientMessage).getBytes());
                        outputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
