package com.goudong.commons.utils.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class IpUtilTest {

    @Test
    void getLongIp() {
    }

    @Test
    void getIpAddress() {
    }

    @Test
    void ipToLong() {
        long ip = IpUtil.ipToLong("192.168.31.184");
        System.out.println("ip = " + ip);
    }

    @Test
    void longToIP() {
        String ip = IpUtil.longToIP(3232243640L);
        System.out.println("ip = " + ip);
    }
}