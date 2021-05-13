import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.oauth2.util.JwtTokenUtil;
import lombok.Data;

/**
 * @Author msi
 * @Date 2021-05-10 11:07
 * @Version 1.0
 */
@Data
public class Demo {
    private String name;
    public static void main(String[] args)  {
        String s = JwtTokenUtil.generateToken(new AuthorityUserDTO(), 2);
        System.out.println("s = " + s);
    }
}
