package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class RSAUtilTest {

    @Test
    void getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAUtil instance = RSAUtil.getInstance();
        System.out.println("instance = " + instance);
    }

    @Test
    void getPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String pubKeyBase64 = RSAUtil.getPubKeyBase64();
        System.out.println("pubKeyBase64 = " + pubKeyBase64);
    }

    @Test
    void pubKeyEncrypt() throws Exception {
        // byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请".getBytes());
        System.out.println("公钥加密后 = " + new String(encrypt));
        byte[] decrypt = RSAUtil.priKeyDecrypt(encrypt);
        System.out.println("私钥解密后 = " + new String(decrypt));
    }

    @Test
    void priKeyEncrypt() throws Exception {
        byte[] encrypt = RSAUtil.priKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        // byte[] encrypt = RSAUtil.priKeyEncrypt("hello world".getBytes());
        System.out.println("私钥加密后 = " + new String(encrypt));
        byte[] decrypt = RSAUtil.pubKeyDecrypt(encrypt);
        System.out.println("公钥解密后 = " + new String(decrypt));
    }

    @Test
    void priKeyDecrypt () throws Exception {
        // String data = "GAIwAupuUresJN6hLgrXH+ZV+Hl3WkUsO7mPp8MGNkb4KK9pVyZ+IbUxH86a4PBBVgqhqJ2F4tlsrp8f5plYQ7eSh/GfVuYkNdQgdNgrff5vnV1L47gUJWl4oHwfCVGdcu7TTEY3ViqPnv9FsE4/Nlrhch2oOdvbUUnsQ0ojTiePUnLD7kE85iH5It8MdUVMIJkNfqXc3/SOPgLR9EmDTWqoL6Dvq2xCjmyc26SO1i1fwe3EHdimOSP8jFtxKEKRbaVGKQsqU/9qS5RUhC7BIL/jjR2+cvvDpvuIP+Hc/lAAvgUcBE2vJ2mKZLuANlgZyQ/huFPCssnrh8Exs38hIy6rooH0LE4tA5ErOSMSyg4XYJzgBGfdXSOD/HB60V3+Xx2OBj0mGUlyVO0jkFBgBLu9zs+g2nMle+O8D/vs3Om3Ch4/PwcXZf2EVw/uZKzD+52jzKLCznB9q2nmxLb9avnbpa8UUAD0CrXBt18AfmGozQ42usk3urDjvdW8acubZy3065jXXsD7jA+u9sNRDs+ylMdgM/3mqW+yvxOg1cN5/4HeZEGhE6BC8+m55YPFmApoEpYl9xVifOVOCEAuGSQaf3hKjsQyII9UOtAI4DsAo1jLNzyGlXMZ/qFrV68LFgt1rChBUvEiQvVnX84ic+coA4tqmJyHyxpPIU+e05FQIhXemuIA4iOLcv9zNbrvkKW7SvqHOv8qNDFLLgLRgo5FDn9ULGQZ++YaAk4MNY/+mgiIU01+zfJ80n0aOt+p/6yjxQDNWY3dE9aanjY2RahzmscKNXmKOAMCwmV1bwmWYFxYQ8dMAQXdld9FMxsv2OfxOkmFsl+yA1dpQr0nbHhn6m0fFtADUON9GxJ/xhJieaCSfIBQ4bbHC29U8a0JXO2DhNRLHeVGWMVJf8DzRqlKbpjYKcTT4Z5iF20OIKQyjOpXdmP7g+0FNMGlLewFvRzAcsqjIOWby2YaGbdDFbr2AEOMYIzNWWNGIhhq+Z6VoVi8THRnHBzo/p9DDGvOcEEOxeE04lGnrRMZtOCvX4tbT4qXAAkH0vOkHgekC/iWGQXJQgkmHK4daSbAubI7qUSEDTvW1b1ggzjhwMkE6Tgw9WtN2caS5pQZ9cdGLhfZUjLvtBTMkPYOuPDva1spLAVzde+gJddhjeE6jZpop8USOHMQTwV77llfaEeuvOkWZC81TUoOkY5cDL8/Tes8A1O4Zi8ufYTu+5GIt+RqnLde9y9nndv0+wsACMZ86wiIgWu67tIwQeAQ9CA7xkdic63VfWJDcmtrXuq1CX8s39PVFuKSu2SAuBhtam1YFGp0PlJqXybbKoEmBMcIPZLpcohqJWdRg3I1MNHbF68xvnB/T1EmL92uAp3KXxGj37ZxfU9SB9s5drcK9LqpLXI6KZI3QkukmdBaEPcmX7ojNC5dw5L+GdyJOjnOiML1eAh1WVMwCF792TI5XZ9FjSQb/eKvUa1EFGifuUQKKw1BROBqnzHN2whzVl6ROHLAWfJZYfdfKPQ+Yyk2aqHk3euA";
        String data = "'\\x04àU\\x8Bè05jU\\tò+¥\\x8E©\\x97 \\x16¢_Ó¶eÀN%å¶Bù\\x9FÞÎ\\x9FÞ\\x92\\x80k\\x849iq\\x85p?õÑ\\x1BôÅÈ\\t72ì\\x94\\x8E\\fáç\u00AD\\x05\\bg9R&Îv©\"µkÏ\\x88\\n\\x18¥Õ\\x8Etì\\x03Ù\\x89)Ö\\x98oÔ\\x97©´ê^\\bþÙß¿Õ\\x98¡Òhy\\x89\\x7Fn\\x14¢r\\x00)\\x92k\\x04]¢GÕÁì4b=ñ\\x99'";
        byte[] bytes = RSAUtil.priKeyDecrypt(data.getBytes());
        System.out.println("new String(bytes) = " + new String(bytes));
    }

    @Test
    void test1() {
        System.out.println();
    }
}