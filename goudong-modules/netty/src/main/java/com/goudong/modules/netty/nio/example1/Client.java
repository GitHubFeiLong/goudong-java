package com.goudong.modules.netty.nio.example1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/5/11 20:04
 */
public class Client {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        /*非阻塞的连接*/
        if(socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080))){
            socketChannel.register(selector,SelectionKey.OP_READ);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isValid()) {
                    if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel)selectionKey.channel();
                        if(selectionKey.isConnectable()){
                            if(channel.finishConnect()){
                                socketChannel.register(selector,
                                        SelectionKey.OP_READ);}
                            else System.exit(1);

                            if (selectionKey.isReadable()) {
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                int read = channel.read(byteBuffer);
                                if (read > 0) {
                                    byteBuffer.flip();
                                    //根据缓冲区可读字节数创建字节数组
                                    byte[] bytes = new byte[byteBuffer.remaining()];
                                    //将缓冲区可读字节数组复制到新建的数组中
                                    byteBuffer.get(bytes);
                                    String result = new String(bytes,"UTF-8");
                                    System.out.println("客户端收到消息：" + result);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
