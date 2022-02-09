// package com.goudong.commons.utils.core;
//
// import cn.hutool.crypto.asymmetric.KeyType;
// import cn.hutool.crypto.asymmetric.RSA;
// import org.junit.jupiter.api.Test;
//
// import java.io.IOException;
// import java.security.NoSuchAlgorithmException;
// import java.security.spec.InvalidKeySpecException;
// import java.util.Base64;
//
// class RSAUtilTest {
//
//     @Test
//     void getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//         RSAUtil instance = RSAUtil.getInstance();
//         System.out.println("instance = " + instance);
//     }
//
//     @Test
//     void getPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//         String pubKeyBase64 = RSAUtil.getServerPubKeyBase64();
//         System.out.println("pubKeyBase64 = " + pubKeyBase64);
//     }
//
//     @Test
//     void pubKeyEncrypt() throws Exception {
//         // byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
//         byte[] encrypt = RSAUtil.serverPubKeyEncrypt("你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请".getBytes());
//         System.out.println("公钥加密后 = " + new String(Base64.getEncoder().encode(encrypt)));
//         byte[] decrypt = RSAUtil.serverPriKeyDecrypt(encrypt);
//         System.out.println("私钥解密后 = " + new String(decrypt));
//     }
//
//     @Test
//     void serverPriKeyEncrypt() throws Exception {
//         String data = "你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请";
//         String s = Base64.getEncoder().encodeToString(data.getBytes());
//         byte[] bytes = RSAUtil.serverPriKeyEncrypt(s.getBytes());
//         System.out.println(Base64.getEncoder().encodeToString(bytes));
//     }
//
//     @Test
//     void serverPubKeyDecrypt() throws Exception {
//         byte[] encrypt = RSAUtil.serverPriKeyEncrypt("你是傻逼吗草密码是123456".getBytes("utf-8"));
//         System.out.println("私钥加密后 = " + new String(Base64.getEncoder().encode(encrypt)));
//         // byte[] decrypt = RSAUtil.serverPubKeyDecrypt(encrypt);
//         // System.out.println("公钥解密后 = " + new String(decrypt,"utf-8"));
//
//         // System.out.println("===========");
//         // 前端私钥加密
//         // String data = "O4pKWZVa8D0/U/j8WEr1sy1uDwdLXvbib/ajNbPYAXtspT6gxc74xpA2gO72NzYPilFamaYAe2eRwgGwcVLXkCbvDNV4JcTYLYeoQpS5zgiTba5i9mvU0Ym03KFBS7nYs0utYaFeIVP/j8x2eD13Xfg8d35STgHX6LvcGTM+8zNbmbp7Aej5eoKht2hOkjymPZBIK9+jcWeslTbyfTuit+yp6r9hEEliAaVkqf8g/5zwEReEWBIx2m4gE9Z7tu9KbXFup9fVVKsv02EysnTBIeRBTQzeF5nIrLYjU5VCS/v1DUG8GR2xErQsrz4QlbnCw8Q7oz8wpp4INqCsUbBxyoV34u1Y7UPE21Dk8Eu4YE4a2eU/063+ri/CMhNcpR78l7pzG3VsMMQpqce5gNlZXn/f2+pIjH2vfegk4qAF+sxfQDuXbAz7tWEdHdf8krYG3A7QO0CWS2bQXcr3P71CD9sQyK6AMqrYbv1rN2AU0E/24UCj2ceMyb66Uu2AY5xHQN6GJym/zxE8mq3BApHOjs/z3Ja1t69ekOfleund2FHdlK7iAYd5Dv7k+bTGuG4zAJzZ/1VLyvl4brpF74AXjqWfNYP1e1HsK7BDQQokRF6uYns+I/eDl7mPYqlIOInKp2pu1S4pU0X57WKPzPbIf7ehMRTPQApaBcPaAFJYBzg=";
//         // byte[] bytes = Base64.getDecoder().decode(data);
//         // byte[] bytes1 = RSAUtil.serverPubKeyDecrypt(bytes);
//
//         String data1 = "MgUkvCMELuivrZSotBsi8ZN/U7edZ1bPSWWIzWdVqepT3aud2Idh8P56pN/z/FwdlzWBqtWn7jOo9Mdcl8V3WFkEGuU0+Yejw+8FtTywSofYKybV8y//mYlHuZrj9fQXNXEAd4D9M2/2U3IoyKEbR4uS6+GSuwpWO3UKpWyySsw=";
//         byte[] bytes1 = RSAUtil.serverPubKeyDecrypt(Base64.getDecoder().decode(data1));
//         System.out.println("公钥解密后 = " + new String(new String(bytes1)));
//         // System.out.println("公钥解密后 = " + new String(Base64.getDecoder().decode(new String(bytes1))));
//     }
//
//     @Test
//     void serverPriKeyDecrypt () throws Exception {
//         // 前端加密并编码后的字符串
//         String data = "VO1WbkG5f6W3702ZuJmK70bhY9MWAoXGyEkQEQ7HxKgurVPJf1qxIMg7p0H2d079P/wrcB0BMoC6d24ZDi1FpS2Fg+7eni3A3iwcFR+XCO5/3qTbbyUojq+7sAlcjsNxDqJZuWarPuMASkUmC6CfBHyOblRbutCvlg32mfuVLnZHSrFfWlZJYnhRiIVv+WGPGlUn5QflI5PV2lb/JTJcxAL6+A1hG3ytn2wOSOh6FJDUCCvnOJ8Nuo/8VGTbtGehbOaXoJsZPqfHitPjuL1VdyjO2Ypm/WOQixclJL0QS5XDY8SEtrU07o5SOurv+oUdsm3SkEaujRa7e0LycEX1MTYGocUf4umRQaglKgh2PTmLafeO/NGsWXP37dILrC+K8cgfmYHvmZFvOAEsQde1Ul7g1r88JftINnFw54hBAIYVV1ULTAGhL7vqkJYFsYvIwfyYrAet5/yi7Ss/NTp0taEKuCMev8MP9FRb3Kqc+T1lL6e83x1Cs6cgcWPtWQTqJIeMQ30cZUyjwrQMoUt7VfCZgDisSwOD/G3X2SB35QnZLyU6QxOhkLTRIeA3fIWbnp6ISssol6nYm/udfs19G6sxEmASk3h2+zRKQLWvETjYnodrYAuTV+CfKS6ecgW5o76SnEvsGfSXYqYR3wc9O38kVxYx4v2AMrEMQLPRHZI=";
//         // 前端加密后的字符串需要使用decode解码。
//         byte[] decode = Base64.getDecoder().decode(data);
//         byte[] bytes = RSAUtil.serverPriKeyDecrypt(decode);
//         System.out.println("new String(bytes) = " + new String(Base64.getDecoder().decode(new String(bytes))));
//     }
//
//     @Test
//     void uiPubKeyDecrypt() throws Exception {
//         String data = "wd2tSlER+06cI6KB/aeg52tJjCl7mP/yyrSJ0SnUG69Pm6utcQe0nW21eIWtWPD3PjT8CpL1xo4Vg6uaQ/xtbgYBMnTBclIbnb6EKRj9CdSyOfKqQ2WB7LlHl6e91de9tHY/tl+svBvI3pqBS5caIbKrPZ7Zs7+9QGzlBFEmG5kCfnLBY0yUgCyuxpIyhdHs2c7xdeW6pyvyyhi4OXGBCLD9anidgto3Ik3mcVKhueXs8nceYTfCDYQbwo2srpqUpgHYqgzpvWx8RAGvSSC+nd5QfU+jFeB4jihiIHP9vDPBHvj5oFJTUFXwbzkBI5yqDTY/6rOtyT1znOqbeBouIZhArg9B/xWQ5nQiDPqk+lMQMdbFWL3kaoLaUO70T51oUxW2r/huYWVFzaS1sNmm7LC7ZHP2p2Colx7XAia7M8AOH+sVjwebP7I/avBRsDyjSPOiwqMPObFL9mbJxleTCWSsN4FE46efkvh/9GPDleaITk+V0cs+Ax9sOpXQj8+kT/nkinL638o7TtDWtNpbg+/K0894wmC7QS7c7i7ECaRDQxWe44cqJWMQ4MbhVyc36kvqM3LnXFvjW2+9Io8gnySKM7xjnxmoKODOiNLc/QdpEqGuQVxuuDBY0XsyUZbtfb54Gv85G+2PLzocJxojg1Qu8gFqL7rjMkd0iks0m1I=";
//         // 前端加密后的字符串需要使用decode解码。
//         byte[] decode = Base64.getDecoder().decode(data);
//         byte[] bytes = RSAUtil.uiPubKeyDecrypt(decode);
//         System.out.println("new String(bytes) = " + new String(bytes));
//     }
//
//     @Test
//     void hutool () throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//         RSA rsa = new RSA(Base64.getEncoder().encodeToString(RSAUtil.getInstance().serverPrivateKey.getEncoded())
//         , Base64.getEncoder().encodeToString(RSAUtil.getInstance().serverPublicKey.getEncoded())
//         );
//
// //         //获得私钥
// //         rsa.getPrivateKey();
// //         rsa.getPrivateKeyBase64();
// // //获得公钥
// //         rsa.getPublicKey();
// //         rsa.getPublicKeyBase64();
// //
// // //公钥加密，私钥解密
// //         byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
// //         byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
// //         Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
// //
// // //私钥加密，公钥解密
// //         byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
//         byte[] decrypt2 = rsa.decrypt("kiOlUIJ7hOtQ+8eJ6v6eMNeix8JuxX0G0Z+3A8dM0SeqfmepeRsz8Lz9PkpODm1IVGdxCpuy2XTuNwSArCsI8P/XabHunAwZZfqIVc7pIswsUZ7/WDu0YLgoDCDTa6+iQ0iD8vZ3ycg5FJ5UK2dD91KjBw7jR0mVGCl9fR8Idx4=".getBytes(),
//                 KeyType.PublicKey);
//         System.out.println("decrypt2 = " + decrypt2);
// //         Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
//     }
// }