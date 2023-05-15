package com.goudong.modules.netty.nio.example1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
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
        // 创建选择器
        Selector selector1 = Selector.open();
        // 创建通道
        SocketChannel socketChannel1 = SocketChannel.open();

        // 创建线程，用以建立连接处理收发数据
        new Thread(){
            private Selector selector = selector1;
            private SocketChannel socketChannel = socketChannel1;
            @Override
            public void run() {
                try {
                    // 设置非阻塞
                    socketChannel.configureBlocking(false);
                    /*非阻塞的连接*/
                    // 连接服务端（返回true，表示连接成功，注册读事件；返回false，表示还未连接成功，注册连接事件）
                    if(socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080))){
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }else{
                        socketChannel.register(selector,SelectionKey.OP_CONNECT);
                    }


                    while (true) {
                        // 获取选择器中就绪的事件
                        selector.select();
                        // 获取就绪事件
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        // 迭代器循环
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            // 获取事件
                            SelectionKey selectionKey = iterator.next();
                            // 我们必须首先将处理过的 SelectionKey 从选定的键集合中删除。如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活的键出现，这会导致我们尝试再次处理它。
                            iterator.remove();

                            // 判断键是否有效，一个键在创建时是有效的，并且一直保持有效，直到它被取消、它的通道被关闭或它的选择器被关闭。
                            if (selectionKey.isValid()) {
                                // 连接事件
                                if(selectionKey.isConnectable()){
                                    // 完成连接后，注册读事件
                                    if(socketChannel.finishConnect()){
                                        socketChannel.register(selector, SelectionKey.OP_READ);}
                                    else {
                                        System.exit(1);
                                    }
                                }
                                // 处理读事件
                                if (selectionKey.isReadable()) {
                                    // 获取注册读事件的客户端SocketChannel
                                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                                    // 创建一个缓冲区
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    // 从通道中读取数据，保存到缓冲区中
                                    int read;

                                    try {
                                        read = channel.read(byteBuffer);
                                    } catch (IOException e) {
                                        System.out.println("服务器关闭服务，断开连接");
                                        read = -1;
                                    }
                                    // 读取到数据
                                    if (read > 0) {
                                        // 切换读模式
                                        byteBuffer.flip();
                                        // 根据缓冲区可读字节数创建字节数组
                                        byte[] bytes = new byte[byteBuffer.remaining()];
                                        // 将缓冲区可读字节数组复制到新建的数组中
                                        byteBuffer.get(bytes);
                                        String result = new String(bytes,"UTF-8");
                                        System.out.println("客户端收到消息：" + result);
                                    } else if (read < 0) {
                                        // 取消键
                                        selectionKey.cancel();
                                        // 关闭通道
                                        channel.close();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            //将消息编码为字节数组
            byte[] bytes = scanner.nextLine().getBytes();
            //根据数组容量创建ByteBuffer
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            //将字节数组复制到缓冲区
            writeBuffer.put(bytes);
            //flip操作
            writeBuffer.flip();
            //发送缓冲区的字节数组
            /*关心事件和读写网络并不冲突*/
            socketChannel1.write(writeBuffer);
        }
    }
}
