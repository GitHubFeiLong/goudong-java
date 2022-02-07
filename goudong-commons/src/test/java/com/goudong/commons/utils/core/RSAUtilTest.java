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
        System.out.println("公钥加密后 = " + Base64.getEncoder().encode(encrypt));
        byte[] decrypt = RSAUtil.serverPriKeyDecrypt(encrypt);
        System.out.println("私钥解密后 = " + new String(decrypt));
    }

    @Test
    void serverPubKeyDecrypt() throws Exception {
        // byte[] encrypt = RSAUtil.serverPriKeyEncrypt("你是傻逼吗草密码是123456".getBytes("utf-8"));
        // System.out.println("私钥加密后 = " + Base64.getEncoder().encode(encrypt));
        // byte[] decrypt = RSAUtil.serverPubKeyDecrypt(encrypt);
        // System.out.println("公钥解密后 = " + new String(decrypt,"utf-8"));

        // System.out.println("===========");
        // 前端私钥加密
        String data = "S8ZMDLOBwSyhxRkKYxgfKGZGRt0riEes85qCGORh0vaNNDUiBFd2lSc9j+udJlxsd9gH7k/pPGdmGFZRufpFIVOSsi8X1CIz3GN8+4djUds9Y0LsXz6fjyzP0l2TPzpdHAwNjPuaDEslEmFCSbZqBvWk1b8xfqHB0dqJh9OlosQLEzi5CFsPt30dafhPWNoOKgIbeISDaqI3fw3GzvTXmZSpOSAWNwV05LtZY+zT1uZ6Gzu/4TokApuFK8EU4jNbr8T8FbbsWxKRoGwueA1paH/kUsSG7/6LmbE/LWLQJptRgsYGKdOsCSHkvodMjJ49MtXbx//NpmyoWCiyKMjtJDiW/u1N6U1KasrgU+Zk6kOpwhqy13BeBTMQNefZA+cPGQicCNW5UDsPrl4Vbr6XPjhhdbJ3+SxWexgt+BO1MZAntOLGNfa8npwP7BZ1vvuURND9jN5M/PPniN8IXYseb4P7DKtIQC6SqaN1sDnuy42IQptWKqgx3lzDCdEx0nMSkziq5olnnMy0QS92kDw9E5ZkcEEjWCNDei8XJug4zwllW9ixGtHKC2Nfdg7DJRnunfwgXk46JU97dpp9H8e/1mqSHUnGn5uk4ryPba+hI+5P4m1rdk8eS2OuyBHUGTOF0kY+ujGu1nmQK7xXu5klRjFcw5mK5yjS+jIjb+EgE1E=";
        byte[] bytes = Base64.getDecoder().decode(data);
        byte[] bytes1 = RSAUtil.serverPubKeyDecrypt(bytes);
        System.out.println("公钥解密后 = " + new String(bytes1,"utf-8"));
    }

    @Test
    void serverPriKeyDecrypt () throws Exception {
        // 前端加密并编码后的字符串
        String data = "BKmOZH/7+ARF/Ite8Dyms+aeH0f77mCk9T2POMi+AoLmR7cuw2DQrw8otqWPZ3bgaWhLAv3sL4JNpiiIbeIvw1hi+D4dmtYYbD0PkeUoNRy34fco2jh8SETAJOJkPb3ncWPECu056qQZ71L2czuKFN+aE9Q8FbHvfL92Q5QdKISPV0rK7XOw52ZZs5WiNdv212WWB+yuCBq+sBaWp1PNFwedL69GSTZbbXagR7EEj4rpA37Vtj0fudPlcl+w78CnlrdYP9XvSYum7Pte/4GmdW/n591ssVjCYO8vReXMBNIBbI0ntZUpacOfx2B7rLi1CbXvJ9bVXrdtV66OrGtnqnWZUirHybnww5yqMIYG5i0mk7XKLnUDOQyj1WIiJkPHVYr1YHwucJUaFd01veALy5F8rR/XJrQ8dEpkyJ1GyiX0jxSXpDgz0KyjvxfpbPM2wD6VgLrLWvNSG+5/EePCvliodoIQwslRhWhd2qMhRvCl7QxiVVkGv+oGjwOgkar/O9aMRXDxdxQaAdSftKPDViFDE6/yR4DEv7+NuxIhG5SQEFePwH9vg5gMrBUQpvvh29Sdy2yKEKxkO0AgBfx34ThnfbFNHIkKOvXur0fOeSAGfivSbJ1ff4OB3v0VS6aezajlaqMjyBYiAaKI8Lurf1gFhDz8gouQVmFXLEKaI7A=";
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