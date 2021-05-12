import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.utils.JwtTokenUtil;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-05-12 22:09
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
        String s = JwtTokenUtil.generateToken(new AuthorityUserDO(), 2);
        System.out.println("s = " + s);
    }
}
