package com.goudong.modules.netty.netty4.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/4/23 11:24
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UnixTime m = (UnixTime) msg;
        System.out.println(m);
        ctx.close();
        /*// 在TCP/IP中，Netty将从对等端发送的数据读入ByteBuf。
        ByteBuf m = (ByteBuf) msg;
        // 首先，将所有接收到的数据累积成buf。
        buf.writeBytes(m);
        m.release();
        // 然后，处理程序必须检查是否有足够的数据(在本例中为4字节)，并继续执行实际的业务逻辑。否则，当更多数据到达时，Netty将再次调用channelRead()方法，最终将累积所有4个字节
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
