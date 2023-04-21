package com.goudong.modules.netty.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 类描述：
 * Handles a server-side channel
 * @author cfl
 * @version 1.0
 * @date 2023/4/21 9:43
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 收到消息时回调
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
        ctx.flush();
    }

    /**
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        System.out.println("执行到了 exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 当建立连接并准备生成流量时，将调用channelActive()方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 为了发送一条新消息，我们需要分配一个包含该消息的新缓冲区。我们将写入一个32位整数，因此我们需要一个容量至少为4字节的ByteBuf。
        // 通过ChannelHandlerContext.alloc()获取当前的ByteBufAllocator并分配一个新的缓冲区。
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                assert f == future;
                ctx.close();
            }
        });
    }
}
