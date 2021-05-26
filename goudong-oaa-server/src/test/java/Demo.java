import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author msi
 * @Date 2021-05-26 10:12
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
