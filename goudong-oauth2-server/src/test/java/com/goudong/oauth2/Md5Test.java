package com.goudong.oauth2;

import cn.hutool.crypto.digest.MD5;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/17 9:16
 */
public class Md5Test {
    public static void main(String[] args) {
        String token =
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ7XCJhdXRob3JpdHlNZW51RFRPU1wiOltdLFwiYXV0aG9yaXR5Um9sZURUT1NcIjpbe1wicmVtYXJrXCI6XCJcIixcInJvbGVOYW1lXCI6XCJST0xFX09SRElOQVJZXCIsXCJyb2xlTmFtZUNuXCI6XCLmma7pgJrnlKjmiLdcIn1dLFwiZW1haWxcIjpcIjE2OTY3NDEwMzhAcXEuY29tXCIsXCJpZFwiOjE0Mjc0MzEzODIxOTU4NTk0NTgsXCJwYXNzd29yZFwiOlwiJDJhJDEwJDNKVWR4N0FxWWVzenhESWtRZUJBMS42ODlPZUxOSDNHTFJkc0g1bmJiNVdyblU3bUlvcThDXCIsXCJwaG9uZVwiOlwiMTUyMTM1MDc3MTZcIixcInVzZXJuYW1lXCI6XCJhZG1pbjFcIn0iLCJzdWIiOiLni5fkuJwiLCJpc3MiOiJjZmwiLCJleHAiOjE2Mjk3NjU5MzYsImlhdCI6MTYyOTE2MTEzNiwianRpIjoiZTQzMTE1ODYtMWQ5Ny00MDJhLWFmMDgtN2YzZDU3ODMyMjZjIn0.NITVBr-9n6qL_PzoFE7PIcx5zKDnNZZi8vL5gsv1uTs"
                ;
        String s = MD5.create().digestHex16(token);
        System.out.println("s = " + s);
    }
}
