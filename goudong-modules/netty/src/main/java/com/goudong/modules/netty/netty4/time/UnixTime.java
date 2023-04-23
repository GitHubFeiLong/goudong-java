package com.goudong.modules.netty.netty4.time;

import java.util.Date;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/4/23 17:43
 */
public class UnixTime {

    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
