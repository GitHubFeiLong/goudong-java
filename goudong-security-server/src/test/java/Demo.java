import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.utils.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-05-12 22:09
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
//        String s = JwtTokenUtil.generateToken(new AuthorityUserDTO(), 2);
//        System.out.println("s = " + s);

//        System.out.println("BCrypt.hashpw(\"123456\", BCrypt.gensalt()) = " + BCrypt.hashpw("123456", BCrypt.gensalt()));
        String s = JwtTokenUtil.generateToken(new AuthorityUserDTO(), 2);
        System.out.println("s = " + s);
    }
}
