package com.goudong.modules.netty.netty4;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/4/21 11:07
 */
public class TimeClient {
    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
    }
}
