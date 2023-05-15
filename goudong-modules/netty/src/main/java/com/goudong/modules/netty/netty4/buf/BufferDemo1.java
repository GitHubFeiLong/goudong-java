package com.goudong.modules.netty.netty4.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/5/15 14:37
 */
public class BufferDemo1 {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes("hello world".getBytes());
        System.out.println("buffer = " + buffer);

        System.out.println(buffer.capacity());
        System.out.println(buffer.writableBytes());
        System.out.println(buffer.readableBytes());
        byte[] bytes = new byte[11];
        buffer.readBytes(bytes);
        System.out.println(new String(bytes));
    }
}
