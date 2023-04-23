package com.goudong.modules.netty.netty4.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 类描述：
 * ByteToMessageDecoder 用来处理碎片问题
 * @author cfl
 * @version 1.0
 * @date 2023/4/23 17:31
 */
public class TimeDecoder extends ByteToMessageDecoder {

    /**
     * 每当接收到新数据时，ByteToMessageDecoder使用内部维护的累积缓冲区调用decode()方法
     * @param ctx           the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in            the {@link ByteBuf} from which to read data
     * @param out           the {@link List} to which decoded messages should be added
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 当累积缓冲区中没有足够的数据时，Decode()可以决定不向out添加任何数据。ByteToMessageDecoder将在接收到更多数据时再次调用decode()。
        if (in.readableBytes() < 4) {
            return;
        }
        // 如果decode()向out添加一个对象，则表示解码器成功解码了一条消息。ByteToMessageDecoder将丢弃累积缓冲区的读部分。请记住，您不需要解码多个消息。ByteToMessageDecoder将继续调用decode()方法，直到它没有向out添加任何内容。
        // out.add(in.readBytes(4));
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
