package com.goudong.commons.security.rsa;

import org.junit.jupiter.api.Test;

class ServerRSAKeyTest {

    /**
     * 公钥加密
     * @throws Exception
     */
    @Test
    void publicKeyEncrypt() throws Exception {
        // SuXXf9wYS1w8GtdDq69111qZzm8djhVoINBePtLrOP8dcnr7GcpDKTySMOU8IeahcSfhTq9vWSkt07HE17yoXdUt+QvMh/fwJ41AChW2QCbpNWnYZsj5HBbHkAKsz3PjaSJ//ePAF0ZSXvJgOexd8XP11fx7XrgaEbinFEOa5eA=
        String data = "你是傻逼啊hello world";
        String s = ServerRSAKey.getInstance().publicKeyEncrypt(data.getBytes());
        System.out.println("s = " + s);
    }

    /**
     * 私钥加密
     * @throws Exception
     */
    @Test
    void privateKeyEncrypt() throws Exception {
        // TbimcFosD5XN97/iJA5wIeuQFr88g/oZkYJ4dq8TxGmtqXbgXF6wV23mZoReaNyiw+ocnJv9UUCka4v3DKHQOVDFU5uEcHJEk7eFNFaT0GZw9qOITvydCnq8n+U1ceGHLq6PVPzKJ5L6OFNOkW+z0MpIzRF8kEV8mE+iLfp7RKs=
        String data = "你是傻逼啊hello world";
        String s = ServerRSAKey.getInstance().privateKeyEncrypt(data.getBytes());
        System.out.println("s = " + s);
    }

    /**
     * 公钥解密
     * @throws Exception
     */
    @Test
    void publicKeyDecrypt() throws Exception {
        // 你是傻逼啊hello world
        String data = "TbimcFosD5XN97/iJA5wIeuQFr88g/oZkYJ4dq8TxGmtqXbgXF6wV23mZoReaNyiw+ocnJv9UUCka4v3DKHQOVDFU5uEcHJEk7eFNFaT0GZw9qOITvydCnq8n+U1ceGHLq6PVPzKJ5L6OFNOkW+z0MpIzRF8kEV8mE+iLfp7RKs=";
        byte[] s = ServerRSAKey.getInstance().publicKeyDecrypt(data);
        System.out.println("s = " + new String(s));
    }

    /**
     * 私钥解密
     * @throws Exception
     */
    @Test
    void privateKeyDecrypt() throws Exception {
        // 你是傻逼啊hello world
        String data = "SuXXf9wYS1w8GtdDq69111qZzm8djhVoINBePtLrOP8dcnr7GcpDKTySMOU8IeahcSfhTq9vWSkt07HE17yoXdUt+QvMh/fwJ41AChW2QCbpNWnYZsj5HBbHkAKsz3PjaSJ//ePAF0ZSXvJgOexd8XP11fx7XrgaEbinFEOa5eA=";
        byte[] s = ServerRSAKey.getInstance().privateKeyDecrypt(data);
        System.out.println("s = " + new String(s));
    }

}