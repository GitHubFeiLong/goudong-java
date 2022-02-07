package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

class RSAUtilTest {

    @Test
    void getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAUtil instance = RSAUtil.getInstance();
        System.out.println("instance = " + instance);
    }

    @Test
    void getPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String pubKeyBase64 = RSAUtil.getServerPubKeyBase64();
        System.out.println("pubKeyBase64 = " + pubKeyBase64);
    }

    @Test
    void pubKeyEncrypt() throws Exception {
        // byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        byte[] encrypt = RSAUtil.serverPubKeyEncrypt("你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请".getBytes());
        System.out.println("公钥加密后 = " + new String(Base64.getEncoder().encode(encrypt)));
        byte[] decrypt = RSAUtil.serverPriKeyDecrypt(encrypt);
        System.out.println("私钥解密后 = " + new String(decrypt));
    }

    @Test
    void serverPriKeyEncrypt() throws Exception {
        String data = "你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请";
        String s = Base64.getEncoder().encodeToString(data.getBytes());
        byte[] bytes = RSAUtil.serverPriKeyEncrypt(s.getBytes());
        System.out.println(Base64.getEncoder().encodeToString(bytes));
    }

    @Test
    void serverPubKeyDecrypt() throws Exception {
        // byte[] encrypt = RSAUtil.serverPriKeyEncrypt("你是傻逼吗草密码是123456".getBytes("utf-8"));
        // System.out.println("私钥加密后 = " + Base64.getEncoder().encode(encrypt));
        // byte[] decrypt = RSAUtil.serverPubKeyDecrypt(encrypt);
        // System.out.println("公钥解密后 = " + new String(decrypt,"utf-8"));

        // System.out.println("===========");
        // 前端私钥加密
        String data = "aaIF8oBQKp+KEjDiyw+ZhSp0onO59y58IqWRv8y4hL34J0McyBrMCQKpf8yIabxucVPrpT8kFOeT3It5CeEsz17f1aYQDRxvwRnqoT2Byea6E9asIe204bV4wk2I8L1Kg61qQKEsvLFHRTednrbCWvFJpq+m9fWslUx2lm2eYLSjJASCEhEuri8RpTtt2oFDlqi/neDwMSVVerLtNtXnaDj95jJd2QdDHndWCV/bLx6iDPGrBn6y267mib9UmK2LdMAme2zgobLUWNG2FMlvWsyN9v+Fz+UPVXG3VvtrpGEUbJAvOYxC+BwnsQMLuKY+ydWBw7ZsZK0gjl7MoOixdxw6bgjK1/zN1sziyIsHkT3IM0cE1PYXL8c83+5bqqlxNyzZ/EWblMK+lqN0/pqEQQvUBDB9fjNDGLc8NAaIhwK6s3dTMLO2OviPJBWIQ1AdaR0sdXVfWSehs0sBIR3KU1qWTbwWPf9MrO+8IxzZwZ9IYG3aj6/WKKPc7xWHUQIfCHJCkCaWJvOwAM/Naxz+EF8TQCsPKKRrLWTLda7pqWR3x7Uv88hb3JWrY/el11AF77t9+s+v1hlCwFjY5JiQPPoBmVmF/nfC3es4XgmOVCKnteux8MpHdAKiLTxUOAVYCzsw2cZ4BdVDsd/dn/qFVyPg8MJVijNjUcWKR4VNNag=";
        byte[] bytes = Base64.getDecoder().decode(data);
        byte[] bytes1 = RSAUtil.serverPubKeyDecrypt(bytes);
        System.out.println("公钥解密后 = " + new String(Base64.getDecoder().decode(new String(bytes1))));
    }

    @Test
    void serverPriKeyDecrypt () throws Exception {
        // 前端加密并编码后的字符串
        String data = "OuPwxLb9itsC/YltpaNIWH4/HD67j42hTy+o06Lb5BN5Q+LlqJP57idB6BHTxiRzMzuGQxh844agV805BsSV4JDqLQ1w9dAC6JHLm4GSKnaY1/+yvUjOh3BuFiCqvSlPtlJ/R5JGf4oI/kp6SZzUhPZnrD25Rt22oUUqPtC0IqGI4pKnI+ZY6mj6Hnz8wpMUdSMF4R+hiUDWIIFN2D6dhzXvR+JmQeYZFzbVGP/Pxx37n8iqJ6wvikgxLZA9c6ghVd57Bw0G938+hMWDWg5V0AXwz3bnMjcFn+7nKxcfnkJgOathqEj9Y8Gjx4RLycwa+adZRan5tmjOMUAGRXz4mwe3iGkRP+b6l/E5MtvpuO/r48wVR36inxTLR2WG0yy+yD0uXeDStkVlSo9E815yuaBCU/e3cYFP+ZhEfiAtESwf3IzNXNveMJb+R5aKFzPVrMrIA9tAmoulZW+J3E7VZp+v0pWozB1iH27TjyrWOVy9uev2wQG/78WhiKpZZRxYJmvTBw9XpHVx9o8PvE1WmnAGtsfcgbCSziWm8xDwcasfAqSSMFheCG6ni3mU38V84ASK22dDN6odPmGyjnmGbXZmh6yZ0QC8xL5gHCTHnKhS49V49c0BW86V1F6z/oPB/LxH2ZiQCM4Hffo8iOpwXl9umcf3lTXqj2QofB9epjw=";
        // 前端加密后的字符串需要使用decode解码。
        byte[] decode = Base64.getDecoder().decode(data);
        byte[] bytes = RSAUtil.serverPriKeyDecrypt(decode);
        System.out.println("new String(bytes) = " + new String(Base64.getDecoder().decode(new String(bytes))));
    }

    @Test
    void uiPubKeyDecrypt() throws Exception {
        String data = "wd2tSlER+06cI6KB/aeg52tJjCl7mP/yyrSJ0SnUG69Pm6utcQe0nW21eIWtWPD3PjT8CpL1xo4Vg6uaQ/xtbgYBMnTBclIbnb6EKRj9CdSyOfKqQ2WB7LlHl6e91de9tHY/tl+svBvI3pqBS5caIbKrPZ7Zs7+9QGzlBFEmG5kCfnLBY0yUgCyuxpIyhdHs2c7xdeW6pyvyyhi4OXGBCLD9anidgto3Ik3mcVKhueXs8nceYTfCDYQbwo2srpqUpgHYqgzpvWx8RAGvSSC+nd5QfU+jFeB4jihiIHP9vDPBHvj5oFJTUFXwbzkBI5yqDTY/6rOtyT1znOqbeBouIZhArg9B/xWQ5nQiDPqk+lMQMdbFWL3kaoLaUO70T51oUxW2r/huYWVFzaS1sNmm7LC7ZHP2p2Colx7XAia7M8AOH+sVjwebP7I/avBRsDyjSPOiwqMPObFL9mbJxleTCWSsN4FE46efkvh/9GPDleaITk+V0cs+Ax9sOpXQj8+kT/nkinL638o7TtDWtNpbg+/K0894wmC7QS7c7i7ECaRDQxWe44cqJWMQ4MbhVyc36kvqM3LnXFvjW2+9Io8gnySKM7xjnxmoKODOiNLc/QdpEqGuQVxuuDBY0XsyUZbtfb54Gv85G+2PLzocJxojg1Qu8gFqL7rjMkd0iks0m1I=";
        // 前端加密后的字符串需要使用decode解码。
        byte[] decode = Base64.getDecoder().decode(data);
        byte[] bytes = RSAUtil.uiPubKeyDecrypt(decode);
        System.out.println("new String(bytes) = " + new String(bytes));
    }
}