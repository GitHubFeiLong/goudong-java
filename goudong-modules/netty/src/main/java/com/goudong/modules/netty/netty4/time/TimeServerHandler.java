package com.goudong.modules.netty.netty4.time;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 类描述：
 * 时间服务器
 * @author cfl
 * @version 1.0
 * @date 2023/4/23 11:39
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当建立连接并准备生成流量时，将调用channelActive()方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelFuture f =ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
        /*// 通过ChannelHandlerContext.alloc()获取当前的ByteBufAllocator并分配一个新的缓冲区。
        ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));

        // ChannelHandlerContext.write()(和writeAndFlush())方法返回一个ChannelFuture。ChannelFuture表示一个尚未发生的I/O操作。这意味着，任何请求的操作都可能尚未执行，因为所有操作在Netty中都是异步的。
        ChannelFuture f = ctx.writeAndFlush(time);

        // 因此，您需要在ChannelFuture完成后调用close()方法(该方法由write()方法返回)，并在写操作完成时通知侦听器。请注意，close()也可能不会立即关闭连接，它返回一个ChannelFuture。
        f.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                assert f == future;
                ctx.close();
            }
        });*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
