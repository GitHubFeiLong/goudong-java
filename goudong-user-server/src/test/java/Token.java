import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.utils.JwtTokenUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author msi
 * @Date 2021-05-24 14:57
 * @Version 1.0
 */
public class Token {
    public static void main(String[] args) {
        // System.out.println(generateToken(AuthorityUserDTO.builder().build(), 1));
        System.out.println("UUID.randomUUID() = " + UUID.randomUUID());
    }

    public static String generateToken (AuthorityUserDTO authorityUserDTO, int hour) {
        // secret 密钥，只有服务器知道
        Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtil.SALT);
        // 当前时间
        LocalDateTime ldt = LocalDateTime.now();

        String token = JWT.create()
                // jwt唯一id
                .withJWTId(IdUtil.randomUUID())
                // 发布者
                .withIssuer(JwtTokenUtil.ISSUER)
                // 生成签名的时间
                .withIssuedAt(new Date())
                // 有效时长
                .withExpiresAt(Date.from(ldt.plusMinutes(hour).atZone(ZoneId.systemDefault()).toInstant()))
                // 绑定用户数据
                .withAudience(JSON.toJSONString(authorityUserDTO))
                // 主题
                .withSubject("狗东")
                // 签发的目标
                .sign(algorithm);

        return "Bearer " + token;
    }
}
