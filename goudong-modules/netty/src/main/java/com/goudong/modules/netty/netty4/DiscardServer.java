package com.goudong.modules.netty.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/4/21 10:02
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        /*
            EventLoopGroup 是一个处理IO操作的多线程事件循环，Netty为不同类型的传输提供了各种EventLoopGroup实现。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // boss 接受传入的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // worker，一旦boss接受连接并将接受的连接注册到worker，worker将处理已接受连接的流量
        try {
            // ServerBootstrap是一个设置服务器的助手类。您可以直接使用Channel设置服务器。但是，请注意，这是一个繁琐的过程，在大多数情况下您不需要这样做。
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // 在这里，我们指定使用NioServerSocketChannel类，该类用于实例化一个新Channel以接受传入的连接。
                    /*
                        这里指定的处理程序将始终由新接受的通道计算。ChannelInitializer是一个特殊的处理程序，用于帮助用户配置新通道。
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    /*
                        你注意到option()和childOption()了吗?
                        option()用于NioServerSocketChannel，它接受传入的连接。
                        childOption()用于父ServerChannel接受的通道，在本例中为NioSocketChannel。
                     */
                    // 可以设置特定于Channel实现的参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 剩下的就是绑定到端口并启动服务器。
            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9090;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }
}
