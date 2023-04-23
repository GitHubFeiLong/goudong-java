package com.goudong.modules.netty.netty4.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/4/21 11:07
 */
public class TimeClient {
    public static void main(String[] args) throws Exception{
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Bootstrap类似于ServerBootstrap，不同之处在于它适用于非服务器通道，如客户端或无连接通道。
            Bootstrap b = new Bootstrap();
            // bossGroup 只用于服务端，这里只能用workGroup
            b.group(workerGroup);
            // 创建客户端通道
            b.channel(NioSocketChannel.class);
            // 客户端SocketChannel没有父类
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 将 TimeDecoder 添加到 ChannelPipeline中
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                }
            });

            // connect 进行连接
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
