package com.goudong.modules.netty.nio.example1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
    /**
     * 端口号
     */
    public static final int PORT = 8080;

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建选择器
        Selector selector = Selector.open();
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定端口，和请求进入的最大连接队列
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT), 1024);
        // 注册事件： 接受连接的事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功");

        while(true) {
            // 获取选择器中就绪的事件
            selector.select();
            // 获取就绪事件的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 创建迭代器
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 获取事件
                SelectionKey selectionKey = iterator.next();
                // 我们必须首先将处理过的 SelectionKey 从选定的键集合中删除。如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活的键出现，这会导致我们尝试再次处理它。
                iterator.remove();
                // 判断该键是否有效。一个键在创建时是有效的，并且一直保持有效，直到它被取消、它的通道被关闭或它的选择器被关闭。
                if (selectionKey.isValid()) {
                    // 处理新接入的客户端的请求
                    if (selectionKey.isAcceptable()) {
                        // 获取注册该事件的通道（上面的ServerSocketChannel）
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        // 接受连接，创建服务端SocketChannel
                        SocketChannel socketChannel = ssc.accept();
                        // 设置为非阻塞
                        socketChannel.configureBlocking(false);
                        // 创建服务端SocketChannel注册事件：读事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端连接成功");
                    } else if (selectionKey.isReadable()) { // 处理读事件
                        // 获取注册读事件的通道（ServerSocket）
                        SocketChannel channel = (SocketChannel)selectionKey.channel();
                        // 创建ByteBuffer，开辟一个缓冲区
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);

                        int read;
                        try {
                            // 从通道内读取数据，然后写入byteBuffer （客户端关闭连接，也会触发读事件，但是读取时会IOException）
                            read = channel.read(byteBuffer);
                        } catch (IOException e) {
                            read = -1;
                        }

                        if (read > 0) {
                            // 切换到读模式
                            byteBuffer.flip();
                            // 创建一个limit - position 大小的字节数组
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            // 从byteBuffer中读取bytes大小的数据。
                            byteBuffer.get(bytes);
                            String msg = new String(bytes);
                            System.out.println("接收到客户端消息: " + msg);
                            // 给客户端做应答
                            channel.write(ByteBuffer.wrap(("服务器收到消息:"+msg).getBytes("UTF-8")));
                        } else if (read < 0) { //
                            System.out.println("远程主机强迫关闭了一个现有的连接。");
                            /*取消特定的注册关系*/
                            selectionKey.cancel();
                            /*关闭通道*/
                            channel.close();
                        }
                    }
                }
            }
        }
    }
}
