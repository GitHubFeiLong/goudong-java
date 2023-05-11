package com.goudong.modules.netty.nio.example1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/5/11 19:49
 */
public class Server {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        serverSocketChannel.socket().bind(new InetSocketAddress(8080), 1024);
        System.out.println("服务启动成功");
        while(true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                boolean valid = selectionKey.isValid();
                if (!valid) {
                    return;
                }

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                } else if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int read = channel.read(byteBuffer);
                    if (read > 0) {
                        System.out.println("接收到消息: " + new String(byteBuffer.array()));
                    } else {
                        channel.close();
                    }
                }
            }
        }
    }
}
